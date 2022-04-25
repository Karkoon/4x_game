package com.mygdx.game.client.initialize;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.client.entityfactory.FieldFactory;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.config.FieldConfig;

import java.util.HashMap;

public class MapInitializer {

  public static HashMap<Coordinates, Entity> initializeMap(FieldFactory fieldFactory, Assets assets) {
    return initializeMap(InitialParameters.INITIAL_WIDTH, InitialParameters.INITIAL_HEIGHT, fieldFactory, assets);
  }

  public static HashMap<Coordinates, Entity> initializeMap(int width, int height, FieldFactory fieldFactory, Assets assets) {
    HashMap<Coordinates, Entity> fieldList = new HashMap<>();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Entity fieldEntity = fieldFactory.createEntity(assets.getGameConfigs().getAny(FieldConfig.class), i, j);
        fieldList.put(new Coordinates(i, j), fieldEntity);
      }
    }
    return fieldList;
  }
}
