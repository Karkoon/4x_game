package com.mygdx.game.client.initialize;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.SlotComponent;
import com.mygdx.game.client.ecs.component.UnitMovementComp;
import com.mygdx.game.client.ecs.entityfactory.UnitFactory;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.config.UnitConfig;

import java.util.Map;

public class StartUnitInitializer {

  public static void initializeTestUnit(UnitFactory unitFactory, GameScreenAssets assets, Map<Coordinates, Entity> fieldList) {
    Coordinates initialCoordinates = new Coordinates(0, 0);
    Entity unitEntity = unitFactory.createEntity(assets.getGameConfigs().getAny(UnitConfig.class), initialCoordinates);
    fieldList.get(initialCoordinates).getComponent(SlotComponent.class).setUnitEntity(unitEntity);
    unitEntity.getComponent(UnitMovementComp.class).setFromAndTo(fieldList.get(initialCoordinates), fieldList.get(initialCoordinates));
  }
}
