package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class CreateUnitService {

  private final Lazy<MessageSender> sender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public CreateUnitService(
      Lazy<MessageSender> sender,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.sender = sender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void createUnit(long unitId, int fieldId) {
    int fieldWorldId = networkWorldEntityMapper.getNetworkEntity(fieldId);

    log.info("create unit for unit " + unitId + " for field " + fieldWorldId + " request send");
    sender.get().send("create_unit" + ":" + unitId + ":" + fieldWorldId);
  }
}
