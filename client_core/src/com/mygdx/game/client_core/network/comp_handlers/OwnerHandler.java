package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Owner;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

public class OwnerHandler implements ComponentMessageListener.Handler {

  private ComponentMapper<Owner> ownerMapper;

  @Inject
  public OwnerHandler(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    ownerMapper.create(worldEntity).setToken(((Owner) component).getToken());
    return FULLY_HANDLED;
  }
}
