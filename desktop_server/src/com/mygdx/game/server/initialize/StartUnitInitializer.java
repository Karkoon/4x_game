package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import com.mygdx.game.server.model.Client;
import lombok.NonNull;

import javax.inject.Inject;

public final class StartUnitInitializer {

  private final UnitFactory unitFactory;
  private final GameScreenAssets assets;

  private boolean initialized = false; // TODO: 16.06.2022 make it support multiple rooms

  @Inject
  public StartUnitInitializer(
      @NonNull UnitFactory unitFactory,
      @NonNull GameScreenAssets assets) {
    this.unitFactory = unitFactory;
    this.assets = assets;
  }

  public void initializeTestUnit(Client owner) {
    if (initialized) {
      return;
    } else {
      initialized = true;
    }
    var initialCoordinates = new Coordinates(0, 0);
    var anyConfig = assets.getGameConfigs().getAny(UnitConfig.class);
    unitFactory.createEntity(anyConfig, initialCoordinates, owner);
  }
}
