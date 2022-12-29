package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@GameInstanceScope

@Log
public class BuildingHandler implements ComponentMessageListener.Handler<Building> {

  private final NetworkWorldEntityMapper networkWorldEntityMapper;
  private ComponentMapper<Building> buildingMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;

  @Inject
  public BuildingHandler(
      NetworkWorldEntityMapper networkWorldEntityMapper,
      World world
  ) {
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Building component) {
    log.info("Read building component " + worldEntity);
    var newParent = component.getParent();
    newParent = networkWorldEntityMapper.getWorldEntity(newParent);
    var building = buildingMapper.create(worldEntity);
    building.setParent(newParent);
    underConstructionMapper.remove(worldEntity);
    return FULLY_HANDLED;
  }
}
