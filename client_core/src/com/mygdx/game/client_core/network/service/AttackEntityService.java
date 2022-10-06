package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class AttackEntityService {

  private final Lazy<WebSocket> socket;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public AttackEntityService(
      Lazy<WebSocket> socket,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.socket = socket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void attack(int attackerEntity, int attackedEntity) {
    var networkAttacker = networkWorldEntityMapper.getNetworkEntity(attackerEntity);
    var networkAttacked = networkWorldEntityMapper.getNetworkEntity(attackedEntity);
    socket.get().send("attack:" + networkAttacker + ":" + networkAttacked);
  }
}
