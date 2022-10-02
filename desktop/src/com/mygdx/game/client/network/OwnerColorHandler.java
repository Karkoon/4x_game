package com.mygdx.game.client.network;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.model.OwnerToColorMap;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Owner;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class OwnerColorHandler implements ComponentMessageListener.Handler<Owner> {

  private final OwnerToColorMap colorMap;
  private ComponentMapper<ModelInstanceComp> modelInstanceMapper;
  private ComponentMapper<Owner> ownerMapper;
  @Inject
  public OwnerColorHandler(
      World world,
      OwnerToColorMap colorMap
  ) {
    this.colorMap = colorMap;
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Owner component) {
    if (modelInstanceMapper.has(worldEntity)) {
      var modelInstance = modelInstanceMapper.get(worldEntity).getModelInstance();
      var owner = ownerMapper.create(worldEntity);
      var color = colorMap.get(owner);
      log.info("color" + color.toIntBits());
      ModelInstanceUtil.tintColor(modelInstance, color);
    }
    return FULLY_HANDLED;
  }
}
