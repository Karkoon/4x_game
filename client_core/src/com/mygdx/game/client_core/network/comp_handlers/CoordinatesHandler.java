package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class CoordinatesHandler implements ComponentMessageListener.Handler<Coordinates> {
  private final ComponentMapper<Coordinates> coordinatesMapper;

  @Inject
  public CoordinatesHandler(
      World world
  ) {
    this.coordinatesMapper = world.getMapper(Coordinates.class);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Coordinates component) {
    log.info("Read coordinates component " + worldEntity);
    coordinatesMapper.create(worldEntity).setCoordinates(component);
    return FULLY_HANDLED;
  }
}
