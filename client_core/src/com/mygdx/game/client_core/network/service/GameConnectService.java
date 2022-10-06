package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.model.PlayerInfo;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class GameConnectService {

  private final WebSocket socket;
  private final PlayerInfo playerInfo;

  @Inject
  public GameConnectService(
      WebSocket socket,
      PlayerInfo playerInfo
  ) {
    this.socket = socket;
    this.playerInfo = playerInfo;
  }

  public void connect() {
    log.info("connect request sent");
    socket.send("connect:" + playerInfo.getUserName() + ":" + playerInfo.getToken() + ":default");
  }

}
