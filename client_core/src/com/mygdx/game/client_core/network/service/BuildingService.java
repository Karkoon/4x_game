package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.Coordinates;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class BuildingService {

  private final Lazy<MessageSender> sender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public BuildingService(
      Lazy<MessageSender> sender,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.sender = sender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void createBuilding(long buildingConfigId, int subfieldEntityId, Coordinates coordinates) {
    log.info("build:" + buildingConfigId + ":" + subfieldEntityId + ":" + coordinates.getX() + ":" + coordinates.getY());
    int networkEntity = networkWorldEntityMapper.getNetworkEntity(subfieldEntityId);
    sender.get().send("build:" + buildingConfigId + ":" + networkEntity + ":" + coordinates.getX() + ":" + coordinates.getY());
  }

}
