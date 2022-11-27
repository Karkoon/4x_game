package com.mygdx.game.server.ecs.system;

import com.artemis.BaseSystem;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.network.gameinstance.StateSyncer;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class FlushNetworkMessagesSystem extends BaseSystem {
  private final StateSyncer stateSyncer;

  @Inject
  public FlushNetworkMessagesSystem(
      StateSyncer stateSyncer
  ) {
    this.stateSyncer = stateSyncer;
  }

  @Override
  protected void processSystem() {
    log.info("flushing messages");
    stateSyncer.flush();
  }
}
