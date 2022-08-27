package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
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
