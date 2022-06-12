package com.mygdx.game.client.network;

import com.github.czyzby.websocket.WebSocket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.inject.Inject;

public class MoveEntityService {

  private final WebSocket webSocket;

  @Inject
  public MoveEntityService(@NonNull WebSocket webSocket) {
    this.webSocket = webSocket;
  }

  public void moveEntity(int entityId, int whereTo) {
    webSocket.send(new MoveRequest(entityId, whereTo));
    // todo ensure entityId, whereTo is a server-entity-Id
  }

  @Data
  @AllArgsConstructor
  private static class MoveRequest {
    int entityId;
    int whereTo;
  }
}
