package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.CreateBuildingService;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class BuildBotHandler extends EntityCommandHandler {

  private final CreateBuildingService createBuildingService;

  @Inject
  public BuildBotHandler(
      CreateBuildingService createBuildingService
  ) {
    this.createBuildingService = createBuildingService;
  }

  public void handle(String[] commands, Client client) {
    var entityConfig = Integer.parseInt(commands[1]);
    var parentField = Integer.parseInt(commands[2]);
    createBuildingService.createBuilding(entityConfig, parentField, client.getGameRoom(), client);
  }
}
