package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class ShowSubfieldService {

  private final Lazy<WebSocket> socket;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public ShowSubfieldService(
      Lazy<WebSocket> socket,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.socket = socket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void flipSubscriptionState(int fieldId) {
    log.info("Show subfields from client to server");
    fieldId = networkWorldEntityMapper.getNetworkEntity(fieldId);
    socket.get().send("field:" + fieldId);
  }
}
