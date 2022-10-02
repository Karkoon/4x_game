package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.Coordinates;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class BuildingService {

  private final Lazy<WebSocket> webSocket;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public BuildingService(
      @NonNull Lazy<com.github.czyzby.websocket.WebSocket> webSocket,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.webSocket = webSocket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void createBuilding(long buildingConfigId, int subfieldEntityId, Coordinates coordinates) {
    log.info("build:" + buildingConfigId + ":" + subfieldEntityId + ":" + coordinates.getX() + ":" + coordinates.getY());
    int networkEntity = networkWorldEntityMapper.getNetworkEntity(subfieldEntityId);
    webSocket.get().send("build:" + buildingConfigId + ":" + networkEntity + ":" + coordinates.getX() + ":" + coordinates.getY());
  }

}
