package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ecs.component.Highlighted;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.network.MoveEntityService;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Highlighted.class, Movable.class})
@Log
public class MovementSystem extends IteratingSystem {

  private final ChosenEntity chosenEntity;
  private final MoveEntityService moveEntityService;
  private ComponentMapper<Highlighted> highlightedMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Field> fieldMapper;
  private final PlayerInfo playerInfo;

  @Inject
  public MovementSystem(
      @NonNull ChosenEntity chosenEntity,
      @NonNull MoveEntityService moveEntityService,
      @NonNull PlayerInfo playerInfo
  ) {
    this.chosenEntity = chosenEntity;
    this.moveEntityService = moveEntityService;
    this.playerInfo = playerInfo;
  }

  @Override
  protected void process(int entityId) {
    if (chosenEntity.isAnyChosen() && fieldMapper.has(chosenEntity.peek())) {
      log.info("some are chosen and there's a movable highlighted entity");
      var targetCoordinate = coordinatesMapper.get(chosenEntity.pop());
      moveEntityIfPossible(entityId, targetCoordinate);
      highlightedMapper.remove(entityId);
    }
  }

  private void moveEntityIfPossible(int entityId, Coordinates targetCoordinate) {
    moveEntityService.moveEntity(entityId, targetCoordinate);
  }
}
