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
public class MoveEntityService {

  private final Lazy<MessageSender> messageSender;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public MoveEntityService(
      Lazy<MessageSender> messageSender,
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.messageSender = messageSender;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  public boolean moveEntity(int selectedUnit, Coordinates coordinates) {
    log.info("Send move message from client to server");
    selectedUnit = networkWorldEntityMapper.getNetworkEntity(selectedUnit);
    messageSender.get().send("move:" + selectedUnit + ":" + coordinates.getX() + ":" + coordinates.getY());
    return true;
  }
}
