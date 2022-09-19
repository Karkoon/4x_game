package com.mygdx.game.server.ecs.system;

import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.server.ecs.component.Dead;
import com.mygdx.game.server.network.gameinstance.services.RemoveClientEntityService;
import dagger.Lazy;

import javax.inject.Inject;

@All({Dead.class})
public class RemoveDeadSystem extends IteratingSystem {

  private Lazy<RemoveClientEntityService> service;

  @Inject
  public RemoveDeadSystem(
      Lazy<RemoveClientEntityService> service
  ) {
    this.service = service;
  }

  @Override
  protected void process(int entityId) {
    service.get().removeEntityForAll(entityId);
    world.delete(entityId);
  }

}
