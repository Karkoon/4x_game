package com.mygdx.game.client.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client.hud.WorldHUD;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class DesktopMaterialHandler implements ComponentMessageListener.Handler<PlayerMaterial> {

  private final WorldHUD worldHUD;
  private ComponentMapper<PlayerMaterial> playerplayerMaterialMapper;

  @Inject
  public DesktopMaterialHandler(
      World world,
      WorldHUD worldHUD
  ) {
    world.inject(this);
    this.worldHUD = worldHUD;
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, PlayerMaterial component) {
    log.info("Desktop read material component " + worldEntity);
    worldHUD.prepareHudSceleton();
    return FULLY_HANDLED;
  }
}
