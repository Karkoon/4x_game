package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.FieldFactory;
import com.mygdx.game.server.ecs.entityfactory.TechnologyFactory;
import com.mygdx.game.server.model.Client;
import lombok.NonNull;

import javax.inject.Inject;

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

  public void initializeTechnologies(Client owner) {
    if (initialized) {
      return;
    }
    initialized = true;
    for (int entityId = GameConfigs.TECHNOLOGY_MIN; entityId < GameConfigs.TECHNOLOGY_MAX; entityId++) {
      var config = assets.getGameConfigs().get(TechnologyConfig.class, entityId);
      technologyFactory.createEntity(config, new Coordinates(config.getX(), config.getY()), owner);
    }
  }

}
