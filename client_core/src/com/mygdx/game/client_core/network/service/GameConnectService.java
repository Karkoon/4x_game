package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.model.PlayerInfo;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class GameConnectService {

  private final Lazy<WebSocket> socket;
  private final PlayerInfo playerInfo;

  @Inject
  public GameConnectService(
      Lazy<WebSocket> socket,
      PlayerInfo playerInfo
  ) {
    this.socket = socket;
    this.playerInfo = playerInfo;
  }

  public void connect() {
    log.info("connect request sent");
    socket.get().send("connect:" + playerInfo.getUserName() + ":" + playerInfo.getToken() + ":default");
  }

}
