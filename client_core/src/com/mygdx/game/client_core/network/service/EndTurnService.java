package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class EndTurnService {

  private final Lazy<WebSocket> socket;

  @Inject
  public EndTurnService(
      Lazy<WebSocket> socket
  ) {
    this.socket = socket;
  }

  public void endTurn() {
    log.info("end turn request send");
    socket.get().send("end_turn");
  }
}
