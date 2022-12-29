package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.TechnologyFactory;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;

import javax.inject.Inject;

@GameInstanceScope
public class TechnologyInitializer {

  private final GameConfigAssets assets;
  private final GameRoom gameRoom;
  private final TechnologyFactory technologyFactory;

  @Inject
  public TechnologyInitializer(
      GameConfigAssets assets,
      GameRoom gameRoom,
      TechnologyFactory technologyFactory
      ) {
    this.assets = assets;
    this.gameRoom = gameRoom;
    this.technologyFactory = technologyFactory;
  }

  public void initializeTechnologies() {
    var clients = gameRoom.getClients();
    for (Client client : clients) {
      setupTechnologiesForClient(client);
    }
  }

  private void setupTechnologiesForClient(Client client) {
    for (int technologyEntityId = GameConfigs.TECHNOLOGY_MIN; technologyEntityId <= GameConfigs.TECHNOLOGY_MAX; technologyEntityId++) {
      var config = assets.getGameConfigs().get(TechnologyConfig.class, technologyEntityId);
      technologyFactory.createEntity(config, client);
    }
  }
}
