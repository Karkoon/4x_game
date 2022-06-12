package com.mygdx.game.server.network;

import com.artemis.ComponentMapper;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.core.model.Coordinates;
import com.mygdx.game.server.ecs.component.UnitMovement;

import javax.inject.Inject;

public class MoveEntityService {

  private final ComponentMapper<UnitMovement> unitMovementMapper;
  private final ComponentMapper<Position> positionMapper;

  @Inject
  MoveEntityService(ComponentMapper<UnitMovement> unitMovementMapper,
                    ComponentMapper<Position> positionMapper) {
    this.unitMovementMapper = unitMovementMapper;
    this.positionMapper = positionMapper;
  }

  public void moveEntity(int entityId, Coordinates toCoordinates) {
    var currentPosition = positionMapper.get(entityId);
    unitMovementMapper.get(entityId);
  }
}
