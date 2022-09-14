package com.mygdx.game.client_core.ecs.system;

import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client_core.model.ToRemove;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;

import javax.inject.Inject;

@All({ToRemove.class})
public class RemovalSystem extends IteratingSystem {

  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public RemovalSystem(
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  @Override
  protected void process(int entityId) {
    networkWorldEntityMapper.removeEntity(entityId, false);
  }
}
