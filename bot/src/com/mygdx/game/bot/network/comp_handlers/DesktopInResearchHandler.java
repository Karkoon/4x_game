package com.mygdx.game.bot.network.comp_handlers;

import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.bot.screen.TechnologyScreen;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.InResearch;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class DesktopInResearchHandler implements ComponentMessageListener.Handler<InResearch> {

  private final TechnologyScreen technologyScreen;

  @Inject
  public DesktopInResearchHandler(
      TechnologyScreen technologyScreen,
      World world
  ) {
    this.technologyScreen = technologyScreen;
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, InResearch component) {
    log.info("Read desktop inResearch handler");
    technologyScreen.refresh();
    return FULLY_HANDLED;
  }
}
