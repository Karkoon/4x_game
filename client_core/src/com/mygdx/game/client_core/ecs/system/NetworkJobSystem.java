package com.mygdx.game.client_core.ecs.system;

import com.artemis.BaseSystem;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.NetworkJobsQueueJobJobberManager;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class NetworkJobSystem extends BaseSystem {

  private final Lazy<NetworkJobsQueueJobJobberManager> networkJobsQueueJobJobberManager;

  @Inject
  public NetworkJobSystem(
      Lazy<NetworkJobsQueueJobJobberManager> networkJobsQueueJobJobberManager
  ) {
    this.networkJobsQueueJobJobberManager = networkJobsQueueJobJobberManager;
  }


  @Override
  protected void processSystem() {
    networkJobsQueueJobJobberManager.get().doAllJobs();
  }
}
