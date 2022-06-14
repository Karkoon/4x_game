package com.mygdx.game.client.network;

import com.github.czyzby.websocket.WebSocket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MoveEntityService {

  private final WebSocket webSocket;
  private final NetworkEntityManager networkEntityManager;

  @Inject
  public MoveEntityService(
      @NonNull WebSocket webSocket,
      NetworkEntityManager networkEntityManager
  ) {
    this.webSocket = webSocket;
    this.networkEntityManager = networkEntityManager;
  }

  public void moveEntity(int selectedUnit, int sourceFieldEntity, int destinationFieldEntity) {
    log.info("Send from client to server");
    selectedUnit = networkEntityManager.getNetworkEntity(selectedUnit);
    sourceFieldEntity = networkEntityManager.getNetworkEntity(sourceFieldEntity);
    destinationFieldEntity = networkEntityManager.getNetworkEntity(destinationFieldEntity);
    webSocket.send("move:" + selectedUnit + ":" + sourceFieldEntity + ":" + destinationFieldEntity);
  }

  @Data
  @AllArgsConstructor
  private static class MoveRequest { // todo create package for common messagges between server and client and use them
    int entityId;
    int whereTo;
  }
}
