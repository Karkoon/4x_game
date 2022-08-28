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

  private final WebSocket socket;

  @Inject
  public GameStartService(@NonNull WebSocket socket) {
    this.socket = socket;
  }

  public void startGame(int width, int height, int mapType) {
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "start game request sent");
    socket.send(String.format("start:%d:%d:%d", width, height, mapType));
  }
}
