package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class GameStartService {

  private final ServerConnection socket;

  @Inject
  public GameStartService(@NonNull ServerConnection connection) {
    this.socket = connection;
  }

  public void startGame(int width, int height, int mapType) {
    log.info("start game request sent");
    socket.send(String.format("start:%d:%d:%d", width, height, mapType));
  }
}
