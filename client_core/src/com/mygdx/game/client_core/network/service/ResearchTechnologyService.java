package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class ResearchTechnologyService {

  private final Lazy<MessageSender> sender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public ResearchTechnologyService(
    Lazy<MessageSender> sender,
    NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.sender = sender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void researchTechnology(int entityId) {
    log.info("research:" + entityId);
    int networkEntity = networkWorldEntityMapper.getNetworkEntity(entityId);
    sender.get().send("research:" + networkEntity);
  }
}
