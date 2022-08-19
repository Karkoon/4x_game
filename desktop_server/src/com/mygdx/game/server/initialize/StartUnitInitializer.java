package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import lombok.NonNull;

import javax.inject.Inject;

public class StartUnitInitializer {

  private final UnitFactory unitFactory;
  private final ComponentFactory componentFactory;
  private final GameConfigAssets assets;

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

  public void initializeTestUnit() {
    if (initialized) {
      return;
    } else {
      initialized = true;
    }
    var anyConfig = assets.getGameConfigs().getAny(UnitConfig.class);
    unitFactory.createEntity(anyConfig, new Coordinates(0, 0));
  }
}
