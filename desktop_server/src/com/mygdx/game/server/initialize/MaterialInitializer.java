package com.mygdx.game.server.initialize;

import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.MaterialFactory;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;

import javax.inject.Inject;

@GameInstanceScope
public class MaterialInitializer {

  private final GameRoom gameRoom;
  private final MaterialFactory materialFactory;

  @Inject
  public MaterialInitializer(
      GameRoom gameRoom,
      MaterialFactory materialFactory
  ) {
    this.gameRoom = gameRoom;
    this.materialFactory = materialFactory;
  }

  public void initializeMaterials() {
    var clients = gameRoom.getClients();
    for (Client client : clients) {
      setUpMaterialsForClients(client);
    }
  }

  private void setUpMaterialsForClients(Client client) {
    for (MaterialBase material : MaterialBase.values()) {
      materialFactory.createEntity(material, client);
    }
  }
}
