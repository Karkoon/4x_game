package com.mygdx.game.server.initialize.field_generators;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.FieldFactory;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.Random;

@GameInstanceScope
public class StableFieldMapGenerator extends MapGenerator {

  private final GameConfigAssets assets;
  private final FieldFactory fieldFactory;
  private final Random random = new Random(0);

  @Inject
  public StableFieldMapGenerator(
      GameConfigAssets assets,
      FieldFactory fieldFactory
  ) {
    super(404);
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
    var chosenField = random.nextInt(14, 17 + 1);
    return assets.getGameConfigs().get(FieldConfig.class, chosenField);
  }
}
