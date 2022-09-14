package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class SubFieldHandler implements ComponentMessageListener.Handler<SubField> {

  private final NetworkWorldEntityMapper networkWorldEntityMapper;
  private final ComponentMapper<SubField> subFieldMapper;

  @Inject
  public SubFieldHandler(
      @NonNull NetworkWorldEntityMapper networkWorldEntityMapper,
      @NonNull World world
  ) {
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    this.subFieldMapper = world.getMapper(SubField.class);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, SubField component) {
    log.info("Read subfield component " + worldEntity);
    var newParent = component.getParent();
    newParent = networkWorldEntityMapper.getWorldEntity(newParent);
    var subField = subFieldMapper.create(worldEntity);
    subField.setParent(newParent);
    return FULLY_HANDLED;
  }
}
