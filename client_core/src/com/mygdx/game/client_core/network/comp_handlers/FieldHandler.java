package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.Field;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class FieldHandler implements ComponentMessageListener.Handler {

  private final NetworkWorldEntityMapper networkWorldEntityMapper;
  private final ComponentMapper<Field> fieldMapper;

  @Inject
  public FieldHandler(
      NetworkWorldEntityMapper networkWorldEntityMapper,
      World world
  ) {
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    this.fieldMapper = world.getMapper(Field.class);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "Read field component " + worldEntity);
    var subFields = ((Field) component).getSubFields();
    for (int i = 0; i < subFields.size; i++) {
      var subField = subFields.get(i);
      subFields.set(i, networkWorldEntityMapper.getWorldEntity(subField));
    }
    var entityField = fieldMapper.create(worldEntity);
    entityField.setSubFields(subFields);
    return FULLY_HANDLED;
  }
}
