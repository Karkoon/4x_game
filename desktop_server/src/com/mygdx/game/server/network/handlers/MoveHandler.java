package com.mygdx.game.server.network.handlers;

import com.mygdx.game.server.network.MoveEntityService;

import javax.inject.Inject;

public class MoveHandler {

  private final MoveEntityService moveEntityService;

  @Inject
  public MoveHandler(
      MoveEntityService moveEntityService
  ) {
    this.moveEntityService = moveEntityService;
  }

  public void handle(String[] commands) {
    var entityId = Integer.parseInt(commands[1]);
    var x = Integer.parseInt(commands[2]);
    var y = Integer.parseInt(commands[3]);
    moveEntityService.moveEntity(entityId, x, y);
  }
}
