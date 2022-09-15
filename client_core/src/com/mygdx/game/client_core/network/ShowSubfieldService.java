package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class ShowSubfieldService {

  private final Lazy<WebSocket> webSocket;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public ShowSubfieldService(
      @NonNull Lazy<WebSocket> webSocket,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.webSocket = webSocket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void flipSubscriptionState(int fieldId) {
    log.info("Show subfields from client to server");
    fieldId = networkWorldEntityMapper.getNetworkEntity(fieldId);
    webSocket.get().send("field:" + fieldId);
  }
}
