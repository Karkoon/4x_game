package com.mygdx.game.client_core.network.message_senders;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.core.ecs.component.Coordinates;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class BuildingService {

  private final Lazy<WebSocket> webSocket;

  @Inject
  public BuildingService(
          @NonNull Lazy<com.github.czyzby.websocket.WebSocket> webSocket
  ) {
    this.webSocket = webSocket;
  }

  public void createBuilding(long buildingConfigId, int subfieldEntityId, Coordinates coordinates) {
    log.info("build:" + buildingConfigId + ":" + subfieldEntityId + ":" + coordinates.getX() + ":" + coordinates.getY());
    webSocket.get().send("build:" + buildingConfigId + ":" + subfieldEntityId + ":" + coordinates.getX() + ":" + coordinates.getY());
  }

}
