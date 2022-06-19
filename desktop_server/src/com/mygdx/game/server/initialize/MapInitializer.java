package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.FieldFactory;
import com.mygdx.game.server.model.Client;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.Random;

public class MapInitializer {

  public static final int INITIAL_WIDTH = 10;
  public static final int INITIAL_HEIGHT = 10;
  private final FieldFactory fieldFactory;
  private final GameScreenAssets assets;
  private final Random random = new Random();

  private boolean initialized = false; // TODO: 16.06.2022 make it support multiple rooms

  @Inject
  public MapInitializer(
          @NonNull FieldFactory fieldFactory,
          @NonNull GameScreenAssets assets
  ) {
    this.fieldFactory = fieldFactory;
    this.assets = assets;
  }

  public void initializeMap(Client owner) {
    initializeMap(INITIAL_WIDTH, INITIAL_HEIGHT, owner);
  }

  public void initializeMap(int width, int height, Client owner) {
    if (initialized) {
      return;
    } else {
      initialized = true;
    }
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        fieldFactory.createEntity(assets
                        .getGameConfigs()
                        .get(FieldConfig.class, 1),
                new Coordinates(i, j),
                clientOwner
        );
        var anyConfig = assets.getGameConfigs().get(FieldConfig.class, random.nextInt(1, GameConfigs.FIELD_AMOUNT+1));
        var coords = new Coordinates(i, j);
        fieldFactory.createEntity(anyConfig, coords, owner);
      }
    }
    fieldFactory.createEntity(assets
            .getGameConfigs()
            .get(FieldConfig.class, 5),
            new Coordinates(8, 8),
            clientOwner
    );

  }
}
