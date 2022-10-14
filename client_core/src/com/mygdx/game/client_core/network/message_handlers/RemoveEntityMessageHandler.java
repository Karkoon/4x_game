package com.mygdx.game.client_core.network.message_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.model.ToRemove;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.network.messages.RemoveEntityMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class RemoveEntityMessageHandler implements QueueMessageListener.Handler<RemoveEntityMessage> {

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
  public boolean handle(WebSocket webSocket, RemoveEntityMessage message) {
    var entityId = mapper.getWorldEntity(message.getEntityId());
    log.info("removed network entity " + message.getEntityId());
    toRemoveComponentMapper.set(entityId, true);
    return true;
  }
}
