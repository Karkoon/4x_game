package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.core.ecs.component.Researched;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@GameInstanceScope
@Log
public class ResearchedHandler implements ComponentMessageListener.Handler<Researched> {

  private ComponentMapper<InResearch> inResearchMapper;
  private ComponentMapper<Researched> researchedMapper;

  @Inject
  public ResearchedHandler(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Researched component) {
    log.info("Read researched handler");
    inResearchMapper.remove(worldEntity);
    researchedMapper.create(worldEntity);
    return FULLY_HANDLED;
  }
}
