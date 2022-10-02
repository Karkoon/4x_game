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
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public CreateUnitService(
      @NonNull Lazy<WebSocket> socket,
      @NonNull NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.socket = socket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void createUnit(long unitId, int fieldId) {
    int fieldWorldId = networkWorldEntityMapper.getNetworkEntity(fieldId);

    log.info("create unit for unit " + unitId + " for field " + fieldWorldId + " request send");
    socket.get().send("create_unit" + ":" + unitId + ":" + fieldWorldId);
  }
}
