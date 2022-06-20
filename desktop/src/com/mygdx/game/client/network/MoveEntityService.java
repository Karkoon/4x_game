package com.mygdx.game.client.network;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.core.ecs.component.Coordinates;
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
    log.info("Move entity " + selectedUnit + " to " + coordinates);
    selectedUnit = networkWorldEntityMapper.getNetworkEntity(selectedUnit);
    webSocket.send("move:" + selectedUnit + ":" + coordinates.getX() + ":" + coordinates.getY());
    // todo create a message to server to deserialize (though the way we have it now is probably faster)
  }
}
