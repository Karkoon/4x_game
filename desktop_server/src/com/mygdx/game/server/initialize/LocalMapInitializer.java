package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.core.initialize.MapInitializer;
import com.mygdx.game.core.model.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.FieldFactory;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class LocalMapInitializer implements MapInitializer {

  public static final int INITIAL_WIDTH = 10;
  public static final int INITIAL_HEIGHT = 10;
  private final FieldFactory fieldFactory;
  private final GameScreenAssets assets;

  @Inject
  public LocalMapInitializer(@NonNull FieldFactory fieldFactory,
                             @NonNull GameScreenAssets assets) {
    this.fieldFactory = fieldFactory;
    this.assets = assets;
  }

  public Map<Coordinates, Integer> initializeMap() {
    return initializeMap(INITIAL_WIDTH, INITIAL_HEIGHT);
  }

  public Map<Coordinates, Integer> initializeMap(int width, int height) {
    var fields = new HashMap<Coordinates, Integer>();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        var anyConfig = assets.getGameConfigs().getAny(FieldConfig.class);
        var coords = Coordinates.of(i, j);
        var field = fieldFactory.createEntity(anyConfig, coords);
        fields.put(coords, field);
      }
    }
    return fields;
  }
}
