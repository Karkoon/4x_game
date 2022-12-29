package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Stats;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@GameInstanceScope
@Log
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
    log.info("Read stats component");
    statsComponentMapper.create(worldEntity).set(component);
    return FULLY_HANDLED;
  }
}
