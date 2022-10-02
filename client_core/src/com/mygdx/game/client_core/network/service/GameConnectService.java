package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.model.PlayerInfo;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class GameConnectService {

  private final WebSocket webSocket;
  private final PlayerInfo playerInfo;

  @Inject
  public GameConnectService(
      @NonNull WebSocket webSocket,
      @NonNull PlayerInfo playerInfo
  ) {
    this.webSocket = webSocket;
    this.playerInfo = playerInfo;
  }

  public void connect() {
    log.info("connect request sent");
    webSocket.send("connect:" + playerInfo.getUserName() + ":" + playerInfo.getToken() + ":default");
  }

}
