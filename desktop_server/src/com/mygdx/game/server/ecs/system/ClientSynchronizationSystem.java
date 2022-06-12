package com.mygdx.game.server.ecs.system;

import com.mygdx.game.server.network.ClientManager;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class ClientSynchronizationSystem extends IntervalSystem {
  private final ClientManager manager;

  @Inject
  public ClientSynchronizationSystem(
      ClientManager manager
  ) {
    super(10_000f);
    this.manager = manager;
  }

  @Override
  protected void processSystem() {
    manager.getClients().values().forEach(socket -> {
          /*
          * dump game state difference from accumulated changes,
          * this is one of the ways to implement the server synchronization,
          * I don't think it's that good, but it might be required?
          *  */
        }
    );
  }
}
