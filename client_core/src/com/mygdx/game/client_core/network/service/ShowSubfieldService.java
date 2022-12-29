package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class ShowSubfieldService {

  private final Lazy<MessageSender> messageSender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public ShowSubfieldService(
      Lazy<MessageSender> messageSender,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.messageSender = messageSender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void flipSubscriptionState(int fieldId) {
    log.info("Show subfields from client to server");
    fieldId = networkWorldEntityMapper.getNetworkEntity(fieldId);
    messageSender.get().send("field:" + fieldId);
  }
}
