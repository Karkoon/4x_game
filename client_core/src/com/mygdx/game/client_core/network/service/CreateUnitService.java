package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class CreateUnitService {

  private final Lazy<WebSocket> socket;

  @Inject
  public CreateUnitService(
      @NonNull Lazy<WebSocket> socket
  ) {
    this.socket = socket;
  }

  public void endTurn() {

  }

  public void createUnit(long unitId, long fieldId) {
    log.info("create unit for unit " + unitId + " for field " + fieldId + " request send");
    socket.get().send("create_unit" + ":" + unitId + ":" + fieldId);
  }
}
