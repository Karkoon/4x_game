package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class AttackEntityService {

  private final Lazy<MessageSender> messasgeSender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public AttackEntityService(
      Lazy<MessageSender> messasgeSender,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.messasgeSender = messasgeSender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void attack(int attackerEntity, int attackedEntity) {
    var networkAttacker = networkWorldEntityMapper.getNetworkEntity(attackerEntity);
    var networkAttacked = networkWorldEntityMapper.getNetworkEntity(attackedEntity);
    messasgeSender.get().send("attack:" + networkAttacker + ":" + networkAttacked);
  }
}
