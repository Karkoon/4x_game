package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.Coordinates;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class BuildingService {

  private final Lazy<MessageSender> messageSender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public BuildingService(
      Lazy<MessageSender> messageSender,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.messageSender = messageSender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public void createBuilding(long buildingConfigId, int subfieldEntityId, Coordinates coordinates) {
    log.info("build:" + buildingConfigId + ":" + subfieldEntityId + ":" + coordinates.getX() + ":" + coordinates.getY());
    int networkEntity = networkWorldEntityMapper.getNetworkEntity(subfieldEntityId);
    messageSender.get().send("build:" + buildingConfigId + ":" + networkEntity + ":" + coordinates.getX() + ":" + coordinates.getY());
  }

  public void createBuilding(long buildingConfigId, int fieldEntityId) {
    log.info("build_bot:" + buildingConfigId + ":" + fieldEntityId);
    int networkEntity = networkWorldEntityMapper.getNetworkEntity(fieldEntityId);
    messageSender.get().send("build_bot:" + buildingConfigId + ":" + networkEntity);
  }

}
