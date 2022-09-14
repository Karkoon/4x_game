package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Stats;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

public class StatsHandler implements ComponentMessageListener.Handler<Stats> {

  private ComponentMapper<Stats> statsComponentMapper;

  @Inject
  public StatsHandler(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Stats component) {
    statsComponentMapper.get(worldEntity).set(component);
    return FULLY_HANDLED;
  }
}
