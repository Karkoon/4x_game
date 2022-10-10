package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class CoordinatesHandler implements ComponentMessageListener.Handler {
  private final ComponentMapper<Coordinates> coordinatesMapper;

  @Inject
  public CoordinatesHandler(
      World world
  ) {
    this.coordinatesMapper = world.getMapper(Coordinates.class);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    log.info("Read coordinates component " + worldEntity);
    var newCoordinates = (Coordinates) component;
    var entityCoordinates = coordinatesMapper.create(worldEntity);
    entityCoordinates.setCoordinates(newCoordinates);
    return FULLY_HANDLED;
  }
}
