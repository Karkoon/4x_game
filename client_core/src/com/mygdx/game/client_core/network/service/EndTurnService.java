package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class EndTurnService {

  private final WebSocket socket;

  @Inject
  public EndTurnService(
      @NonNull WebSocket socket
  ) {
    this.socket = socket;
  }

  public void endTurn() {
    log.info("end turn request send");
    socket.send("end_turn");
  }
}
