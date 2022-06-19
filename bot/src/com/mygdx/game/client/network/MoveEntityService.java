package com.mygdx.game.client.network;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MoveEntityService {

  private final WebSocket webSocket;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public MoveEntityService(
      @NonNull WebSocket webSocket,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.webSocket = webSocket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void moveEntity(int selectedUnit, Coordinates coordinates) {
    log.info("Send from client to server");
    selectedUnit = networkWorldEntityMapper.getNetworkEntity(selectedUnit);
    webSocket.send("move:" + selectedUnit + ":" + coordinates.getX() + ":" + coordinates.getY());
  }

  @Data
  @AllArgsConstructor
  private static class MoveRequest { // todo create package for common messagges between server and client and use them
    int entityId;
    int whereTo;
  }
}
