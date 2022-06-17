package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.FieldFactory;
import lombok.NonNull;

import javax.inject.Inject;

public class MapInitializer {

  public static final int INITIAL_WIDTH = 10;
  public static final int INITIAL_HEIGHT = 10;
  private final FieldFactory fieldFactory;
  private final GameScreenAssets assets;

  @Inject
  public MapInitializer(
      @NonNull FieldFactory fieldFactory,
      @NonNull GameScreenAssets assets
  ) {
    this.fieldFactory = fieldFactory;
    this.assets = assets;
  }

  public void initializeMap(int clientOwner) {
    initializeMap(INITIAL_WIDTH, INITIAL_HEIGHT, clientOwner);
  }

  public void initializeMap(int width, int height, int clientOwner) {
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        var anyConfig = assets.getGameConfigs().getAny(FieldConfig.class);
        var coords = new Coordinates(i, j);
        fieldFactory.createEntity(anyConfig, coords, clientOwner);
      }
    }
  }
}