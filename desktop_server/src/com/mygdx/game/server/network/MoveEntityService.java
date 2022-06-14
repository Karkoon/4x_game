package com.mygdx.game.server.network;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Slot;
import com.mygdx.game.core.network.ComponentMessage;
import com.mygdx.game.server.ecs.component.UnitMovement;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MoveEntityService {

  private final ComponentMapper<UnitMovement> unitMovementMapper;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Slot> slotMapper;

  @Inject
  MoveEntityService(World world) {
    this.unitMovementMapper = world.getMapper(UnitMovement.class);
    this.positionMapper = world.getMapper(Position.class);
    this.slotMapper = world.getMapper(Slot.class);
  }

  public ComponentMessage<Position> moveEntity(String unitEntity, String fromEntity, String toEntity) {
    var unit = Integer.parseInt(unitEntity);
    var from = Integer.parseInt(fromEntity);
    var to = Integer.parseInt(toEntity);
    var unitPosition = positionMapper.get(unit);
    var goalPosition = positionMapper.get(to);

    unitPosition.setPosition(goalPosition.getPosition());
    var unitMovement = unitMovementMapper.create(unit);
    unitMovement.setFromSlot(slotMapper.get(from));
    unitMovement.setToSlot(slotMapper.get(to));

    log.info("Send position component");
    return new ComponentMessage<>(goalPosition, unit);
  }
}
