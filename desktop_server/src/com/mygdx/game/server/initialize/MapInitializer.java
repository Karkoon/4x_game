package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
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
  private static final int WINNING_X = 4;
  private static final int WINNING_Y = 4;
  private final FieldFactory fieldFactory;
  private final GameConfigAssets assets;
  private final Random random = new Random();
  private boolean initialized = false; // TODO: 16.06.2022 make it support multiple rooms

  @Inject
  public MapInitializer(
      @NonNull FieldFactory fieldFactory,
      @NonNull GameConfigAssets assets
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
        if (i != WINNING_X || j != WINNING_Y) {
          var config = assets
              .getGameConfigs()
              .get(FieldConfig.class, random.nextInt(GameConfigs.FIELD_MIN, GameConfigs.FIELD_MAX));
          fieldFactory.createEntity(config, new Coordinates(i, j), owner);
        }
      }
    }
    createWinningField(owner);
  }

  private void createWinningField(Client owner) {
    fieldFactory.createEntity(assets
            .getGameConfigs()
            .get(FieldConfig.class, 5),
        new Coordinates(4, 4),
        owner
    );
  }
}
