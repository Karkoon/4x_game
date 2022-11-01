package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
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

  public void attack(int attackerEntity, int attackedEntity) { // todo think if the conditons should be checked here or not
    var networkAttacker = networkWorldEntityMapper.getNetworkEntity(attackerEntity);
    var networkAttacked = networkWorldEntityMapper.getNetworkEntity(attackedEntity);
    sender.get().send("attack:" + networkAttacker + ":" + networkAttacked);
  }
}
