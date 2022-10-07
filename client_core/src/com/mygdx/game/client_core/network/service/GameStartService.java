package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class GameStartService {

  private final Lazy<WebSocket> socket;

  @Inject
  public GameStartService(
      Lazy<WebSocket> socket
  ) {
    this.socket = socket;
  }

  public void startGame(int width, int height, int mapType) {
    log.info("start game request sent");
    socket.get().send(String.format("start:%d:%d:%d", width, height, mapType));
  }
}
