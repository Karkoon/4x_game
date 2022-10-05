package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.PlayerMaterialComponent;
import com.mygdx.game.core.ecs.component.MaterialComponent;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class MaterialHandler implements ComponentMessageListener.Handler<PlayerMaterialComponent>  {

  private final ComponentMapper<PlayerMaterialComponent> playerplayerMaterialMapper;

  @Inject
  public MaterialHandler(
      World world
  ) {
    this.playerplayerMaterialMapper = world.getMapper(PlayerMaterialComponent.class);
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, PlayerMaterialComponent component) {
    log.info("Read material component " + worldEntity);
    var material = playerplayerMaterialMapper.create(worldEntity);
    material.setMaterial(material.getMaterial());
    material.setValue(material.getValue());
    return FULLY_HANDLED;
  }

}
