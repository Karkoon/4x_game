package com.mygdx.game.server.initialize.field_generators;

import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.FieldFactory;

import javax.inject.Inject;
import java.util.Random;

@GameInstanceScope
public class BotWinningFieldMapGenerator extends MapGenerator {

  private static final int WINNING_X = 4;
  private static final int WINNING_Y = 4;
  private final GameConfigs assets;
  private final FieldFactory fieldFactory;
  private final Random random = new Random(0);

  @Inject
  public BotWinningFieldMapGenerator(
      GameConfigs assets,
      FieldFactory fieldFactory
  ) {
    super(401);
    this.assets = assets;
    this.fieldFactory = fieldFactory;
  }

  @Override
  public void generateMap(int width, int height) {
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (i == WINNING_X && j == WINNING_Y) {
          continue;
        }
        var fieldConfig = chooseFieldConfig();
        fieldFactory.createEntity(fieldConfig, new Coordinates(i, j));
      }
    }
    createWinningField();
  }

  private FieldConfig chooseFieldConfig() {
    return assets.getAll(FieldConfig.class).random();
  }

  private void createWinningField() {
    var winningConfig = assets.get(FieldConfig.class, 5);
    fieldFactory.createEntity(winningConfig, new Coordinates(WINNING_X, WINNING_Y));
  }
}
