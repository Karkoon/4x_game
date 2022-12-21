package com.mygdx.game.server.initialize.field_generators;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.FieldFactory;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.Random;

@GameInstanceScope
public class RandomFieldMapGenerator extends MapGenerator {

  private static final int WINNING_X = 4;
  private static final int WINNING_Y = 4;
  private final GameConfigAssets assets;
  private final FieldFactory fieldFactory;
  private final Random random = new Random(0);

  @Inject
  public RandomFieldMapGenerator(
      GameConfigAssets assets,
      FieldFactory fieldFactory
  ) {
    super(401);
    this.assets = assets;
    this.fieldFactory = fieldFactory;
  }

  @Override
  public IntArray generateMap(int width, int height) {
    var fieldIds = new IntArray(width * height);
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        var fieldConfig = chooseFieldConfig();
        var entityId = fieldFactory.createEntity(fieldConfig, new Coordinates(i, j));
        fieldIds.add(entityId);
      }
    }
    return fieldIds;
  }

  private @NonNull FieldConfig chooseFieldConfig() {
    var chosenField = random.nextInt(GameConfigs.FIELD_MIN, GameConfigs.FIELD_MAX);
    return assets.getGameConfigs().get(FieldConfig.class, chosenField);
  }
}
