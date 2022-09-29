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
public class MaterialHandler implements ComponentMessageListener.Handler<MaterialComponent>  {

  private final ComponentMapper<MaterialComponent> materialMapper;

  @Inject
  public MaterialHandler(
      World world
  ) {
    this.materialMapper = world.getMapper(MaterialComponent.class);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, MaterialComponent component) {
    log.info("Read material component " + worldEntity);
    var material = materialMapper.create(worldEntity);
    material.setMaterial(material.getMaterial());
    material.setValue(material.getValue());
    return FULLY_HANDLED;
  }

}
