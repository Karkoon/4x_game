package com.mygdx.game.client_core.network.message_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.client_core.model.ToRemove;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.network.messages.RemoveEntityMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class RemoveEntityMessageHandler implements WebSocketHandler.Handler<RemoveEntityMessage> {

  private final NetworkWorldEntityMapper mapper;
  private ComponentMapper<ToRemove> toRemoveComponentMapper;

  @Inject
  public RemoveEntityMessageHandler(
      World world,
      NetworkWorldEntityMapper mapper
  ) {
    world.inject(this);
    this.mapper = mapper;
  }

  @Override
  public boolean handle(WebSocket webSocket, RemoveEntityMessage removeEntityMessage) {
    var entityId = mapper.getWorldEntity(removeEntityMessage.getEntityId());
    log.info("removed network entity " + removeEntityMessage.getEntityId());
    toRemoveComponentMapper.set(entityId, true);
    return true;
  }
}
