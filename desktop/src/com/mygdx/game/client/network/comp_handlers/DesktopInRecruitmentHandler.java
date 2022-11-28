package com.mygdx.game.client.network.comp_handlers;

import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client.hud.FieldScreenHUD;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.InRecruitment;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class DesktopInRecruitmentHandler implements ComponentMessageListener.Handler<InRecruitment> {

  private final FieldScreenHUD fieldScreenHUD;

  @Inject
  public DesktopInRecruitmentHandler(
      FieldScreenHUD fieldScreenHUD,
      World world
  ) {
    this.fieldScreenHUD = fieldScreenHUD;
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, InRecruitment component) {
    log.info("Read desktop inRecruitment handler");
    fieldScreenHUD.prepareHudSceleton();
    return FULLY_HANDLED;
  }
}
