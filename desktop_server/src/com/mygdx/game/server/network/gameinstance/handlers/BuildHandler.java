package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.CreateBuildingService;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class BuildHandler extends EntityCommandHandler {

  private final CreateBuildingService createBuildingService;

  @Inject
  public BuildHandler(
      CreateBuildingService createBuildingService
  ) {
    this.createBuildingService = createBuildingService;
  }

  public void handle(String[] commands, Client client) {
    var entityConfig = Integer.parseInt(commands[1]);
    var parentField = Integer.parseInt(commands[2]);
    var x = Integer.parseInt(commands[3]);
    var y = Integer.parseInt(commands[4]);
    var clientIndex = client.getGameRoom().getClients().indexOf(client);
    createBuildingService.createBuilding(entityConfig, parentField, x, y, client.getGameRoom(), clientIndex);
  }
}
