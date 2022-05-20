package com.mygdx.game.client.initialize;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.entityfactory.FieldFactory;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.config.FieldConfig;

import java.util.HashMap;
import java.util.Map;

public class MapInitializer {

  public static Map<Coordinates, Entity> initializeMap(FieldFactory fieldFactory, GameScreenAssets assets) {
    return initializeMap(InitialParameters.INITIAL_WIDTH, InitialParameters.INITIAL_HEIGHT, fieldFactory, assets);
  }

  public static Map<Coordinates, Entity> initializeMap(int width, int height, FieldFactory fieldFactory, GameScreenAssets assets) {
    var fieldList = new HashMap<Coordinates, Entity>();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Coordinates coords = new Coordinates(i, j);
        var fieldEntity = fieldFactory.createEntity(assets.getGameConfigs().getAny(FieldConfig.class), coords);
        fieldList.put(coords, fieldEntity);
      }
    }
    return fieldList;
  }
}
