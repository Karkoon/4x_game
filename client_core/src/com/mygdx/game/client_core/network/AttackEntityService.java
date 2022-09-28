package com.mygdx.game.client_core.network;

import com.github.czyzby.websocket.WebSocket;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class AttackEntityService {

  private final Lazy<WebSocket> webSocket;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public AttackEntityService(
      Lazy<WebSocket> webSocket,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.webSocket = webSocket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void attack(int attackerEntity, int attackedEntity) {
    var networkAttacker = networkWorldEntityMapper.getNetworkEntity(attackerEntity);
    var networkAttacked = networkWorldEntityMapper.getNetworkEntity(attackedEntity);
    webSocket.get().send("attack:" + networkAttacker + ":" + networkAttacked);
  }
}
