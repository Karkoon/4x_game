package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Research;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class ResearchHandler implements ComponentMessageListener.Handler<Research> {

  private ComponentMapper<Research> reserchedMapper;

  @Inject
  public ResearchHandler(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Research component) {
    log.info("Read research handler");
    reserchedMapper.create(worldEntity);
    return FULLY_HANDLED;
  }
}
