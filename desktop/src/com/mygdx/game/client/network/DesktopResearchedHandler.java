package com.mygdx.game.client.network;

import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client.screen.TechnologyScreen;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Researched;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class DesktopResearchedHandler implements ComponentMessageListener.Handler<Researched> {

  private final TechnologyScreen technologyScreen;

  @Inject
  public DesktopResearchedHandler(
      TechnologyScreen technologyScreen,
      World world
  ) {
    this.technologyScreen = technologyScreen;
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Researched component) {
    log.info("Read desktop researched handler");
    technologyScreen.refresh();
    return FULLY_HANDLED;
  }
}
