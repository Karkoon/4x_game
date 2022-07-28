package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import com.mygdx.game.server.model.Client;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.Random;

public class StartUnitInitializer {

  private final UnitFactory unitFactory;
  private final ComponentFactory componentFactory;
  private final GameConfigAssets assets;
  private final Random random = new Random();

  private boolean initialized = false; // TODO: 16.06.2022 make it support multiple rooms

  @Inject
  public StartUnitInitializer(
      @NonNull UnitFactory unitFactory,
      @NonNull ComponentFactory componentFactory,
      @NonNull GameConfigAssets assets) {
    this.unitFactory = unitFactory;
    this.componentFactory = componentFactory;
    this.assets = assets;
  }

  public void initializeTestUnit(Client owner) {
    if (initialized) {
      return;
    } else {
      initialized = true;
    }
    int entityId = componentFactory.createEntityId();
    componentFactory.createCoordinateComponent(new Coordinates(0, 0), entityId);
    var anyConfig = assets.getGameConfigs().get(UnitConfig.class, random.nextInt(GameConfigs.UNIT_MAX - GameConfigs.UNIT_MIN) + GameConfigs.UNIT_MIN);
    unitFactory.createEntity(entityId, anyConfig, owner);
  }
}
