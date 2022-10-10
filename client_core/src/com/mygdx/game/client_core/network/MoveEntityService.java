package com.mygdx.game.client_core.network;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class MoveEntityService {

  private final ServerConnection serverConnection;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public MoveEntityService(
      @NonNull ServerConnection serverConnection,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.serverConnection = serverConnection;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void moveEntity(int selectedUnit, Coordinates coordinates) {
    log.info("Send from client to server");
    selectedUnit = networkWorldEntityMapper.getNetworkEntity(selectedUnit);
    serverConnection.send("move:" + selectedUnit + ":" + coordinates.getX() + ":" + coordinates.getY());
  }
}
