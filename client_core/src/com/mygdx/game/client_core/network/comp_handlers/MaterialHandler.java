package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@GameInstanceScope
@Log
public class MaterialHandler implements ComponentMessageListener.Handler<PlayerMaterial>  {

  private ComponentMapper<PlayerMaterial> playerMaterialMapper;

  @Inject
  public MaterialHandler(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, PlayerMaterial component) {
    log.info("Read material component " + worldEntity);
    var material = playerMaterialMapper.create(worldEntity);
    material.setMaterial(component.getMaterial());
    material.setValue(component.getValue());
    return FULLY_HANDLED;
  }

}
