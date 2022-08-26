package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.BuildingService;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class BuildHandler extends EntityCommandHandler {

  private final BuildingService buildingService;

  @Inject
  public BuildHandler(
      BuildingService buildingService
  ) {
    this.buildingService = buildingService;
  }

  public void handle(String[] commands, Client client) {
    var entityConfig = Integer.parseInt(commands[1]);
    var parentField = Integer.parseInt(commands[2]);
    var x = Integer.parseInt(commands[3]);
    var y = Integer.parseInt(commands[4]);
    buildingService.createBuilding(entityConfig, parentField, x, y, client.getGameRoom());
  }
}
