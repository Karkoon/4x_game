package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Field;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class FieldHandler implements ComponentMessageListener.Handler<Field> {

  private ComponentMapper<Field> fieldMapper;

  @Inject
  public FieldHandler(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Field component) {
    log.info("Read field component " + worldEntity);
    fieldMapper.create(worldEntity).setSubFields(component.getSubFields());
    return FULLY_HANDLED;
  }
}
