package com.mygdx.game.client.network;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client.hud.WorldHUD;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Set;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;
import static com.github.czyzby.websocket.WebSocketListener.NOT_HANDLED;
import static com.mygdx.game.client_core.ecs.entityfactory.Setter.Result.HANDLED;
import static com.mygdx.game.client_core.ecs.entityfactory.Setter.Result.HANDLING_ERROR;

@Log
public class DesktopMaterialHandler implements ComponentMessageListener.Handler<PlayerMaterial> {

  private final World world;
  private final WorldHUD worldHUD;
  private ComponentMapper<PlayerMaterial> playerplayerMaterialMapper;

  @Inject
  public DesktopMaterialHandler(
      World world,
      WorldHUD worldHUD
  ) {
    world.inject(this);
    this.world = world;
    this.worldHUD = worldHUD;
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, PlayerMaterial component) {
    log.info("Read material component " + worldEntity);
    var material = playerplayerMaterialMapper.create(worldEntity);
    material.setMaterial(component.getMaterial());
    material.setValue(component.getValue());
    worldHUD.prepareHudSceleton();
    return FULLY_HANDLED;
  }
}
