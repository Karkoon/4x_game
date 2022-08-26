package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.TechnologyFactory;
import lombok.NonNull;

import javax.inject.Inject;

@GameInstanceScope
public class TechnologyInitializer {

  private final TechnologyFactory technologyFactory;
  private final GameConfigAssets assets;

  private boolean initialized = false; // TODO: 16.06.2022 make it support multiple rooms

  @Inject
  public TechnologyInitializer(
      @NonNull TechnologyFactory technologyFactory,
      @NonNull GameConfigAssets assets
  ) {
    this.technologyFactory = technologyFactory;
    this.assets = assets;
  }

  public void initializeTechnologies() {
    if (initialized) {
      return;
    }
    initialized = true;
    for (int technologyEntityId = GameConfigs.TECHNOLOGY_MIN; technologyEntityId < GameConfigs.TECHNOLOGY_MAX; technologyEntityId++) {
      var config = assets.getGameConfigs().get(TechnologyConfig.class, technologyEntityId);
      technologyFactory.createEntity(config);
    }
  }

}
