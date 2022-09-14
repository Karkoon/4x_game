package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class AttackEntityService {

  private final Lazy<WebSocket> webSocket;

  @Inject
  public AttackEntityService(
      Lazy<WebSocket> webSocket
  ) {
    this.webSocket = webSocket;
  }

  public void attack(int attackerEntity, int attackedEntity) {
    webSocket.get().send("attack:" + attackerEntity + ":" + attackedEntity);
  }
}
