package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.TechnologyFactory;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import lombok.NonNull;

import javax.inject.Inject;

@GameInstanceScope
public class TechnologyInitializer {

  private final TechnologyFactory technologyFactory;
  private final GameConfigAssets assets;
  private final GameRoom gameRoom;

  @Inject
  public TechnologyInitializer(
      @NonNull TechnologyFactory technologyFactory,
      @NonNull GameConfigAssets assets,
      @NonNull GameRoom gameRoom
  ) {
    this.technologyFactory = technologyFactory;
    this.assets = assets;
    this.gameRoom = gameRoom;
  }

  public void initializeTechnologies() {
    var clients = gameRoom.getClients();
    for (int i = 0; i < clients.size(); i++) {
      var client = clients.get(i);
      setupTechnologiesForClient(client);
    }
  }

  private void setupTechnologiesForClient(Client client) {
    for (int technologyEntityId = GameConfigs.TECHNOLOGY_MIN; technologyEntityId < GameConfigs.TECHNOLOGY_MAX; technologyEntityId++) {
      var config = assets.getGameConfigs().get(TechnologyConfig.class, technologyEntityId);
      technologyFactory.createEntity(config, client);
    }
  }
}
