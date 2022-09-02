package com.mygdx.game.client_core.network;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class EndTurnService {

  private final ServerConnection serverConnection;

  @Inject
  public EndTurnService(
      @NonNull ServerConnection serverConnection
  ) {
    this.serverConnection = serverConnection;
  }

  public void endTurn() {
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "end turn request send");
    serverConnection.send("end_turn");
  }
}
