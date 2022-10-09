package com.mygdx.game.client_core.network.service;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class ResearchTechnologyService {

  private final Lazy<WebSocket> socket;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public ResearchTechnologyService(
    Lazy<WebSocket> socket,
    NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.socket = socket;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void researchTechnology(int entityId) {
    log.info("research:" + entityId);
    int networkEntity = networkWorldEntityMapper.getNetworkEntity(entityId);
    socket.get().send("research:" + networkEntity);
  }
}
