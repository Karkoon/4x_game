package com.mygdx.game.client.initialize;

import com.github.czyzby.websocket.WebSocket;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MapService {

  private final WebSocket socket;

  @Inject
  public MapService(@NonNull WebSocket socket) {
    this.socket = socket;
  }

  public void initializeMap() {
    log.info("map initialize request sent");
    socket.send("map");
  }
}
