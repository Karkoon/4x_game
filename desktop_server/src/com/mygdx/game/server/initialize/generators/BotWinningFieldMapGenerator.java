package com.mygdx.game.server.initialize.generators;

import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.ecs.entityfactory.FieldFactory;

import javax.inject.Inject;
import java.util.Random;

public class BotWinningFieldMapGenerator extends MapGenerator {

  private static final int WINNING_X = 4;
  private static final int WINNING_Y = 4;
  private final GameConfigs assets;
  private final FieldFactory fieldFactory;
  private final ComponentFactory componentFactory;
  private final Random random = new Random(0);

  @Inject
  public BotWinningFieldMapGenerator(
      GameConfigs assets,
      FieldFactory fieldFactory,
      ComponentFactory componentFactory
  ) {
    super(10);
    this.assets = assets;
    this.fieldFactory = fieldFactory;
    this.componentFactory = componentFactory;
  }

  @Override
  public void generateMap(int width, int height) {
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (i == WINNING_X && j == WINNING_Y) {
          continue;
        }
        var entityId = componentFactory.createEntityId();
        componentFactory.createCoordinateComponent(new Coordinates(i, j), entityId);
        var fieldConfig = chooseFieldConfig();
        fieldFactory.createEntity(entityId, fieldConfig);
      }
    }
    createWinningField();
  }

  private FieldConfig chooseFieldConfig() {
    var fieldConfigs = assets.getAll(FieldConfig.class);
    var chosenField = random.nextInt(fieldConfigs.size);
    return fieldConfigs.get(chosenField);
  }

  private void createWinningField() {
    var winningConfig = assets.get(FieldConfig.class, 5);
    int entityId = componentFactory.createEntityId();
    componentFactory.createCoordinateComponent(new Coordinates(4, 4), entityId);
    fieldFactory.createEntity(entityId, winningConfig);
  }
}
