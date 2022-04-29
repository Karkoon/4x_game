package com.mygdx.game.client.initialize;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.component.FieldComponent;
import com.mygdx.game.client.entityfactory.UnitFactory;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.config.UnitConfig;

import java.util.Map;

public class StartUnitInitializer {

  public static void initializeTestUnit(UnitFactory unitFactory, GameScreenAssets assets, Map<Coordinates, Entity> fieldList) {
    Coordinates initialCoordinates = new Coordinates(0, 0);
    Entity unitEntity = unitFactory.createEntity(assets.getGameConfigs().getAny(UnitConfig.class), initialCoordinates);
    fieldList.get(initialCoordinates).getComponent(FieldComponent.class).setUnitEntity(unitEntity);
  }
}
