package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.MoveEntityService;

import javax.inject.Inject;

public class MoveHandler extends EntityCommandHandler {

  private final MoveEntityService moveEntityService;

  @Inject
  public MoveHandler(
      MoveEntityService moveEntityService
  ) {
    this.moveEntityService = moveEntityService;
  }

  public void handle(String[] commands, Client client) {
    if (!checkValidity(commands, client)) return;
    var entityId = Integer.parseInt(commands[1]);
    var x = Integer.parseInt(commands[2]);
    var y = Integer.parseInt(commands[3]);
    moveEntityService.moveEntity(entityId, x, y, client.getGameRoom());
  }
}
