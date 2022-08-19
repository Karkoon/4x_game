package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.ecs.entityfactory.TechnologyFactory;
import lombok.NonNull;

import javax.inject.Inject;

public class TechnologyInitializer {

  private final TechnologyFactory technologyFactory;
  private final ComponentFactory componentFactory;
  private final GameConfigAssets assets;

  private boolean initialized = false; // TODO: 16.06.2022 make it support multiple rooms

  @Inject
  public TechnologyInitializer(
      @NonNull TechnologyFactory technologyFactory,
      @NonNull ComponentFactory componentFactory,
      @NonNull GameConfigAssets assets
  ) {
    this.technologyFactory = technologyFactory;
    this.componentFactory = componentFactory;
    this.assets = assets;
  }

  public void initializeTechnologies() {
    if (initialized) {
      return;
    }
    initialized = true;
    var technologyConfigs = assets.getGameConfigs().getAll(TechnologyConfig.class);
    for (var config : technologyConfigs) {
      technologyFactory.createEntity(config);
    }
  }

}
