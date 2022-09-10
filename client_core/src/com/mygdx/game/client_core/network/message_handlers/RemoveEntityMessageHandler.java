package com.mygdx.game.client_core.network.message_handlers;

import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.core.network.messages.RemoveEntityMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class RemoveEntityMessageHandler implements WebSocketHandler.Handler<RemoveEntityMessage> {

  private final NetworkWorldEntityMapper mapper;

  @Inject
  public RemoveEntityMessageHandler(
      NetworkWorldEntityMapper mapper
  ) {
    this.mapper = mapper;
  }

  @Override
  public boolean handle(WebSocket webSocket, RemoveEntityMessage removeEntityMessage) {
    var entityId = removeEntityMessage.getEntityId();
    log.info("removed network entity " + entityId);
    mapper.removeEntity(entityId, true);
    // todo implement a queue to gather these messages and apply them in a system to ensure better
    // data integrity
    return true;
  }
}
