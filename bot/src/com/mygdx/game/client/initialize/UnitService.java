package com.mygdx.game.client.initialize;

import com.github.czyzby.websocket.WebSocket;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class UnitService {

  private final WebSocket webSocket;

  @Inject
  public UnitService(@NonNull WebSocket webSocket) {
    this.webSocket = webSocket;
  }

  public void initializeTestUnit() {
    log.info("unit initialize request sent");
    webSocket.send("unit"); // TODO: 15.06.2022 make this a message for the server to process
  }
}
