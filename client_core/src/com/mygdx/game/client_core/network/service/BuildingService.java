package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.Coordinates;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class BuildingService {

  private final Lazy<WebSocket> socket;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public BuildingService(
      Lazy<com.github.czyzby.websocket.WebSocket> socket,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.socket = socket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void createBuilding(long buildingConfigId, int subfieldEntityId, Coordinates coordinates) {
    log.info("build:" + buildingConfigId + ":" + subfieldEntityId + ":" + coordinates.getX() + ":" + coordinates.getY());
    int networkEntity = networkWorldEntityMapper.getNetworkEntity(subfieldEntityId);
    socket.get().send("build:" + buildingConfigId + ":" + networkEntity + ":" + coordinates.getX() + ":" + coordinates.getY());
  }

}
