package com.mygdx.game.bot.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.bot.model.ChosenEntity;
import com.mygdx.game.bot.ui.OutOfMoveRangeDialogFactory;
import com.mygdx.game.bot.ecs.component.Highlighted;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.network.service.MoveEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.util.DistanceUtil;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@All({Highlighted.class, Movable.class})
@GameInstanceScope
public class MovementSystem extends IteratingSystem {

  private final ChosenEntity chosenEntity;
  private final MoveEntityService moveEntityService;
  private final OutOfMoveRangeDialogFactory moveRangeDialog;
  private ComponentMapper<Highlighted> highlightedMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Stats> statsMapper;
  private ComponentMapper<Field> fieldMapper;

  @Inject
  public MovementSystem(
      ChosenEntity chosenEntity,
      MoveEntityService moveEntityService,
      OutOfMoveRangeDialogFactory moveRangeDialog
  ) {
    this.chosenEntity = chosenEntity;
    this.moveEntityService = moveEntityService;
    this.moveRangeDialog = moveRangeDialog;
  }

  @Override
  protected void process(int entityId) {
    if (chosenEntity.isAnyChosen() && fieldMapper.has(chosenEntity.peek())) {
      log.info("some are chosen and there's a movable highlighted entity");

      var targetCoordinate = coordinatesMapper.get(chosenEntity.pop());
      var currentCoordinate = coordinatesMapper.get(entityId);

      var distance = DistanceUtil.distance(currentCoordinate, targetCoordinate);
      var range = statsMapper.get(entityId).getMoveRange();

      log.info("Chce przejsc " + distance + " ale zostało mi " + range);

      if (distance > range){
        moveRangeDialog.createAndShow(range, distance);
      }
      else {
        moveEntityService.moveEntity(entityId, targetCoordinate);
        highlightedMapper.remove(entityId);
      }
    }
  }

}
