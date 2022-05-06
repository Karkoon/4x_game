package com.mygdx.game.client.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.client.component.PositionComponent;
import com.mygdx.game.client.component.SlotComponent;

public class MovementUtil {

  private static final ComponentMapper<SlotComponent> slotComponentMapper = ComponentMapper.getFor(SlotComponent.class);
  private static final ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);

  public void moveUnit(Entity oldFieldEntity, Entity newFieldEntity) {
    Entity unitEntity = clearOldEntityAndGetUnit(oldFieldEntity);
    setNewEntity(unitEntity, newFieldEntity);
  }

  private Entity clearOldEntityAndGetUnit(Entity fieldEntity) {
    var oldSlotComponent = slotComponentMapper.get(fieldEntity);
    Entity unitEntity = oldSlotComponent.getUnitEntity();
    oldSlotComponent.setUnitEntity(null);
    return unitEntity;
  }

  private void setNewEntity(Entity unitEntity, Entity fieldEntity) {
    var newSlotComponent = slotComponentMapper.get(fieldEntity);
    newSlotComponent.setUnitEntity(unitEntity);
    setNewPosition(unitEntity, fieldEntity);
  }

  private void setNewPosition(Entity unitEntity, Entity fieldEntity) {
    var fieldPositionComponent = positionComponentMapper.get(fieldEntity);
    var unitPositionComponent = positionComponentMapper.get(unitEntity);
    unitPositionComponent.setPosition(fieldPositionComponent.getPosition());
  }

}
