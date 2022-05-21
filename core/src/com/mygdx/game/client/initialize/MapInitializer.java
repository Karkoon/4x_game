package com.mygdx.game.client.initialize;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.entityfactory.FieldFactory;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.config.FieldConfig;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class MapInitializer {

  private final FieldFactory fieldFactory;
  private final GameScreenAssets assets;

  public static final int INITIAL_WIDTH = 10;
  public static final int INITIAL_HEIGHT = 10;

  @Inject
  public MapInitializer(@NonNull FieldFactory fieldFactory,
                        @NonNull GameScreenAssets assets) {
    this.fieldFactory = fieldFactory;
    this.assets = assets;
  }

  public Map<Coordinates, Entity> initializeMap() {
    return initializeMap(INITIAL_WIDTH, INITIAL_HEIGHT);
  }

  public Map<Coordinates, Entity> initializeMap(int width, int height) {
    var fieldList = new HashMap<Coordinates, Entity>();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        var coords = new Coordinates(i, j);
        var anyConfig = assets.getGameConfigs().getAny(FieldConfig.class);
        var fieldEntity = fieldFactory.createEntity(anyConfig, coords);
        fieldList.put(coords, fieldEntity);
      }
    }
    return fieldList;
  }
}
