package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ecs.component.Highlighted;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client.ui.OutOfMoveRangeDialogFactory;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.network.MoveEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.MoveRange;
import com.mygdx.game.core.util.DistanceUtil;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Highlighted.class, Movable.class})
@Log
public class MovementSystem extends IteratingSystem {

  private final ChosenEntity chosenEntity;
  private final MoveEntityService moveEntityService;
  private final OutOfMoveRangeDialogFactory moveRangeDialog;
  private ComponentMapper<Highlighted> highlightedMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<MoveRange> moveRangeMapper;
  private ComponentMapper<Field> fieldMapper;

  @Inject
  public MovementSystem(
      @NonNull ChosenEntity chosenEntity,
      @NonNull MoveEntityService moveEntityService,
      @NonNull OutOfMoveRangeDialogFactory moveRangeDialog
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
      log.info(String.valueOf(moveRangeMapper.get(entityId).getMoveRange()));
      var range = moveRangeMapper.get(entityId).getCurrentRange();

      if (distance > range){
        moveRangeDialog.createAndShow("You have " + range + " move points left!" + "You can't move " + distance + " units!");
      }
      else {
        moveEntityService.moveEntity(entityId, targetCoordinate);
        highlightedMapper.remove(entityId);
      }
    }
  }

}
