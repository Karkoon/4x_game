package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.model.GameState;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class CoordinatesHandler implements ComponentMessageListener.Handler {
  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final GameState gameState;

  @Inject
  public CoordinatesHandler(
      NetworkWorldEntityMapper networkWorldEntityMapper,
      World world,
      GameState gameState
  ) {
    this.gameState = gameState;
    this.coordinatesMapper = world.getMapper(Coordinates.class);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    log.info("Read coordinates component " + worldEntity);
    var newCoordinates = (Coordinates) component;
    var entityCoordinates = coordinatesMapper.create(worldEntity);
    gameState.removeEntity(worldEntity);
    entityCoordinates.setCoordinates(newCoordinates);
    gameState.saveEntity(worldEntity);
    return FULLY_HANDLED;
  }
}
