package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class AttackEntityService {

  private final Lazy<MessageSender> sender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public AttackEntityService(
      Lazy<MessageSender> sender,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.sender = sender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void attack(int attackerEntity, int attackedEntity) {
    var networkAttacker = networkWorldEntityMapper.getNetworkEntity(attackerEntity);
    var networkAttacked = networkWorldEntityMapper.getNetworkEntity(attackedEntity);
    sender.get().send("attack:" + networkAttacker + ":" + networkAttacked);
  }
}
