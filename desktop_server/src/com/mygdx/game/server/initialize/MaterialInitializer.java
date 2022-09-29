package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.MaterialFactory;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import lombok.NonNull;

import javax.inject.Inject;

@GameInstanceScope
public class MaterialInitializer {

  private final MaterialFactory materialFactory;
  private final GameConfigAssets assets;
  private final GameRoom gameRoom;

  @Inject
  public MaterialInitializer(
      @NonNull MaterialFactory materialFactory,
      @NonNull GameConfigAssets assets,
      @NonNull GameRoom gameRoom
  ) {
    this.materialFactory = materialFactory;
    this.assets = assets;
    this.gameRoom = gameRoom;
  }

  public void initializeMaterials() {
    var clients = gameRoom.getClients();
    for (int i = 0; i < clients.size(); i++) {
      var client = clients.get(i);
      setUpMaterialsForClients(client);
    }
  }

  private void setUpMaterialsForClients(Client client) {
    for (MaterialBase material : MaterialBase.values()) {
      materialFactory.createEntity(material, client);
    }
  }
}
