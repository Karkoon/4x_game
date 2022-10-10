package com.mygdx.game.client_core.network;

import com.mygdx.game.client_core.model.PlayerInfo;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class GameConnectService {

  private final ServerConnection connection;
  private final PlayerInfo playerInfo;

  @Inject
  public GameConnectService(
      @NonNull ServerConnection connection,
      @NonNull PlayerInfo playerInfo
  ) {
    this.connection = connection;
    this.playerInfo = playerInfo;
  }

  public void connect(String roomId) {
    log.info("connect request sent");
    connection.send("connect:" + playerInfo.getUserName() + ":" + playerInfo.getToken() + ":" + roomId);
  }

}
