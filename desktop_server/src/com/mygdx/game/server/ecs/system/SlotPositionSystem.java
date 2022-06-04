package com.mygdx.game.server.ecs.system;


import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Slot;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@All({Slot.class, Position.class})
public class SlotPositionSystem extends IteratingSystem {
  private ComponentMapper<Slot> slotMapper;
  private ComponentMapper<Position> positionMapper;

  @Inject
  public SlotPositionSystem() {
  }

  @Override
  protected void process(int entityId) {
    log.info("");
    var slotPosition = positionMapper.get(entityId);
    var slotEntities = slotMapper.get(entityId).getEntities();

    for (int i = 0; i < slotEntities.size; i++) {
      var slottedEntity = slotEntities.get(i);
      var slottedEntityPosition = positionMapper.get(slottedEntity);
      slottedEntityPosition.set(slotPosition);
    }
  }
}
