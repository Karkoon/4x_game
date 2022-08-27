package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.core.ecs.component.Coordinates;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class MoveEntityService {

  private final Lazy<WebSocket> webSocket;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public MoveEntityService(
      @NonNull Lazy<WebSocket> webSocket,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.webSocket = webSocket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void moveEntity(int selectedUnit, Coordinates coordinates) {
    log.info("Send from client to server");
    selectedUnit = networkWorldEntityMapper.getNetworkEntity(selectedUnit);
    webSocket.get().send("move:" + selectedUnit + ":" + coordinates.getX() + ":" + coordinates.getY());
  }
}
