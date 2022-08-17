package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import lombok.NonNull;

import javax.inject.Inject;

@GameInstanceScope
public class StartUnitInitializer {

  private final UnitFactory unitFactory;
  private final GameConfigAssets assets;

  @Inject
  public StartUnitInitializer(
      @NonNull UnitFactory unitFactory,
      @NonNull GameConfigAssets assets
  ) {
    this.unitFactory = unitFactory;
    this.assets = assets;
  }

  public void initializeTestUnit() {
    var anyConfig = assets.getGameConfigs().getAny(UnitConfig.class);
    unitFactory.createEntity(anyConfig, new Coordinates(0, 0));
  }
}
