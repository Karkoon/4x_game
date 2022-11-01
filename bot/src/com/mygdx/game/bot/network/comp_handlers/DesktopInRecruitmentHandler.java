package com.mygdx.game.bot.network.comp_handlers;

import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.bot.hud.InfieldHUD;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.InRecruitment;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class DesktopInRecruitmentHandler implements ComponentMessageListener.Handler<InRecruitment> {

  private final InfieldHUD infieldHUD;

  @Inject
  public DesktopInRecruitmentHandler(
      InfieldHUD infieldHUD,
      World world
  ) {
    this.infieldHUD = infieldHUD;
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, InRecruitment component) {
    log.info("Read desktop inRecruitment handler");
    infieldHUD.prepareHudSceleton();
    return FULLY_HANDLED;
  }
}
