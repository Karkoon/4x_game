package com.mygdx.game.server.network;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Slot;
import com.mygdx.game.core.network.ComponentMessage;
import com.mygdx.game.server.ecs.component.UnitMovement;
import io.vertx.core.json.Json;

import javax.inject.Inject;

public class MoveEntityService {

  private final ComponentMapper<UnitMovement> unitMovementMapper;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Slot> slotMapper;
  private final ClientManager clientManager;

  @Inject
  MoveEntityService(World world,
                    ClientManager clientManager) {
    this.unitMovementMapper = world.getMapper(UnitMovement.class);
    this.positionMapper = world.getMapper(Position.class);
    this.slotMapper = world.getMapper(Slot.class);
    this.clientManager = clientManager;
  }

  public ComponentMessage moveEntity(String unitEntity, String fromEntity, String toEntity) {
    Integer unit = Integer.valueOf(unitEntity);
    Integer from = Integer.valueOf(fromEntity);
    Integer to = Integer.valueOf(toEntity);
    var unitPosition = positionMapper.get(unit);
    var goalPosition = positionMapper.get(to);

    unitPosition.setPosition(goalPosition.getPosition());
    var unitMovement = unitMovementMapper.create(unit);
    unitMovement.setFromSlot(slotMapper.get(from));
    unitMovement.setToSlot(slotMapper.get(to));

    System.out.println("Send position component");
    ComponentMessage componentMessagePosition = new ComponentMessage(goalPosition, unit);
    return componentMessagePosition;

  }
}
