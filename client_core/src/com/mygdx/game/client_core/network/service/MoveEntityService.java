package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.Coordinates;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MoveEntityService {

  private final Lazy<WebSocket> socket;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public MoveEntityService(
      Lazy<WebSocket> socket,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.socket = socket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void moveEntity(int selectedUnit, Coordinates coordinates) {
    log.info("Send from client to server");
    selectedUnit = networkWorldEntityMapper.getNetworkEntity(selectedUnit);
    socket.get().send("move:" + selectedUnit + ":" + coordinates.getX() + ":" + coordinates.getY());
  }
}
