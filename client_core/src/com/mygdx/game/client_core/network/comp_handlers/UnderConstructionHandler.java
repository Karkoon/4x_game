package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class UnderConstructionHandler implements ComponentMessageListener.Handler<UnderConstruction> {


  private final NetworkWorldEntityMapper networkWorldEntityMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;
  private ComponentMapper<Position> positionMapper;

  @Inject
  public UnderConstructionHandler(
      NetworkWorldEntityMapper networkWorldEntityMapper,
      World world
  ) {
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, UnderConstruction component) {
    log.info("Read construction component " + worldEntity);

    var newParent = component.getParentSubfield();
    newParent = networkWorldEntityMapper.getWorldEntity(newParent);

    var underConstruction = underConstructionMapper.create(worldEntity);
    underConstruction.setParentSubfield(newParent);

    positionMapper.create(worldEntity).getValue().set(0, 10, 0);

    return FULLY_HANDLED;
  }

}
