package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
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

  public void connect(String roomId) {
    log.info("connect request sent");
    webSocket.send("connect:" + playerInfo.getUserName() + ":" + playerInfo.getToken() + ":" + roomId);
  }

}
