package com.mygdx.game.client.initialize;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.Slot;
import com.mygdx.game.client.ecs.component.UnitMovement;
import com.mygdx.game.client.ecs.entityfactory.UnitFactory;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.config.UnitConfig;

import java.util.Map;

public final class StartUnitInitializer {

  private StartUnitInitializer() {

  }

  public static void initializeTestUnit(UnitFactory unitFactory, GameScreenAssets assets, Map<Coordinates, Entity> fieldList) {
    var initialCoordinates = new Coordinates(0, 0);
    var unitEntity = unitFactory.createEntity(assets.getGameConfigs().getAny(UnitConfig.class), initialCoordinates);
    fieldList.get(initialCoordinates).getComponent(Slot.class).setUnitEntity(unitEntity);
    unitEntity.getComponent(UnitMovement.class).setFromAndTo(fieldList.get(initialCoordinates), fieldList.get(initialCoordinates));
  }
}
