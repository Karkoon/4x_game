package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.MaterialComponent;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class MaterialHandler implements ComponentMessageListener.Handler  {

  private final ComponentMapper<MaterialComponent> materialMapper;

  @Inject
  public MaterialHandler(
      World world
  ) {
    this.materialMapper = world.getMapper(MaterialComponent.class);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    log.info("Read material component " + worldEntity);
    var material = (MaterialComponent) component;
    if (!materialMapper.has(worldEntity)) {
      materialMapper.create(worldEntity);
      materialMapper.get(worldEntity).setMaterial(material.getMaterial());
    }
    materialMapper.get(worldEntity).setValue(material.getValue());
    return FULLY_HANDLED;
  }

}
