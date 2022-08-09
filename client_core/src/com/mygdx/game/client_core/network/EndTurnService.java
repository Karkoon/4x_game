package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class EndTurnService {

  private final WebSocket socket;
  private final PlayerInfo playerInfo;

  @Inject
  public EndTurnService(
      @NonNull WebSocket socket,
      @NonNull PlayerInfo playerInfo
  ) {
    this.socket = socket;
    this.playerInfo = playerInfo;
  }

  public void endTurn() {
    log.info("end turn request send");
    socket.send("end_turn" + ":" + playerInfo.getUserName());
  }
}
