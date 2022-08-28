package com.mygdx.game.server.network.handlers;

import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoomManager;

import javax.inject.Inject;

public class CloseHandler {

  private final GameRoomManager manager;

  @Inject
  public CloseHandler(
      GameRoomManager manager
  ) {
    this.manager = manager;
    // todo remove gameroom when no players are connected
    // when something like this happens, the current way
    // uses the same room because the clients don't support
    // different room ids yet and the server is prepared for the case
    // when the same group of players (still connected to the server)
    // will want to start a new game
    // but fully implementing this will require changes to both
    // client and server
  }

  public void handle(Client client) {
    if (client.getGameRoom() != null) {
      client.getGameRoom().removeClient(client);
    }
  }
}
