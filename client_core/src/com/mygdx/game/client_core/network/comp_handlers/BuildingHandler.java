package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.Building;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class BuildingHandler implements ComponentMessageListener.Handler<Building> {

  private final NetworkWorldEntityMapper networkWorldEntityMapper;
  private final ComponentMapper<Building> buildingMapper;

  @Inject
  public BuildingHandler(
      @NonNull NetworkWorldEntityMapper networkWorldEntityMapper,
      @NonNull World world
  ) {
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    this.buildingMapper = world.getMapper(Building.class);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Building component) {
    log.info("Read building component " + worldEntity);
    var newParent = ((Building) component).getParent();
    newParent = networkWorldEntityMapper.getWorldEntity(newParent);
    var building = buildingMapper.create(worldEntity);
    building.setParent(newParent);
    return FULLY_HANDLED;
  }
}
