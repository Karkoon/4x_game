package com.mygdx.game.client.initialize;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.core.model.Coordinates;
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

  public void initializeTestUnit(Coordinates coordinates) {
    log.info("unit initialize request sent");
    webSocket.send("unit");
  }
}
