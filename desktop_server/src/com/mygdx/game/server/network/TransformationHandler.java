package com.mygdx.game.server.network;

import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler.Handler;
import com.mygdx.game.server.ecs.component.UnitMovement;

// TODO: 04.06.2022 use it for networking?
public class TransformationHandler implements Handler<UnitMovement> {

  private final World world;

  public TransformationHandler(final World world) {
    this.world = world;
  }

  @Override
  public boolean handle(WebSocket webSocket, UnitMovement transformationComp) {

    return false;
  }
}
