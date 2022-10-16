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

@Log
@GameInstanceScope
public class MaterialHandler implements ComponentMessageListener.Handler<PlayerMaterial>  {
  // TODO: 15.10.2022 check if the class is actually used somewhere
  private final World world;
  private ComponentMapper<PlayerMaterial> playerplayerMaterialMapper;

  @Inject
  public MaterialHandler(
      World world
  ) {
    world.inject(this);
    this.world = world;
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, PlayerMaterial component) {
    log.info("Read material component " + worldEntity);
    var material = playerplayerMaterialMapper.create(worldEntity);
    material.setMaterial(component.getMaterial());
    material.setValue(component.getValue());
    return FULLY_HANDLED;
  }

}
