package com.mygdx.game.client.network;

import com.github.czyzby.websocket.WebSocket;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class GameStartService {

  private final WebSocket socket;

  @Inject
  public GameStartService(@NonNull WebSocket socket) {
    this.socket = socket;
  }

  public void startGame(int width, int height) {
    log.info("start game request sent");
    socket.send(String.format("start:%d:%d", width, height));
  }
}
