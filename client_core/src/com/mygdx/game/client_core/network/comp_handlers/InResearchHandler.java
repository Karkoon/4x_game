package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.InResearch;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@GameInstanceScope
@Log
public class InResearchHandler implements ComponentMessageListener.Handler<InResearch> {

  private ComponentMapper<InResearch> reserchedMapper;

  @Inject
  public InResearchHandler(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, InResearch component) {
    log.info("Read inresearch handler");
    var inResearch = reserchedMapper.create(worldEntity);
    inResearch.setConfigRequiredScience(component.getConfigRequiredScience());
    inResearch.setScienceLeft(component.getScienceLeft());
    return FULLY_HANDLED;
  }
}
