package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class GameConnectService {

  private final WebSocket webSocket;

  @Inject
  public GameConnectService(
      @NonNull WebSocket webSocket
  ) {
    this.webSocket = webSocket;
  }

  public void connect() {
    log.info("connect request sent");
    webSocket.send("connect");
  }

}
