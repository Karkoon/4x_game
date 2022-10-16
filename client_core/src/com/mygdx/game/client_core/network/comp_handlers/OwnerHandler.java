package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Owner;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class OwnerHandler implements ComponentMessageListener.Handler<Owner> {

  private ComponentMapper<Owner> ownerMapper;

  @Inject
  public OwnerHandler(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Owner component) {
    log.info("added onwer component or changed owner component");
    ownerMapper.create(worldEntity).setToken(component.getToken());
    return FULLY_HANDLED;
  }
}
