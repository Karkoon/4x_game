package com.mygdx.game.client_core.ecs.system;

import com.artemis.BaseSystem;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceNetworkModule;
import com.mygdx.game.client_core.model.NetworkJobsQueueJobJobberManager;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
public class NetworkJobSystem extends BaseSystem {

  private final Lazy<NetworkJobsQueueJobJobberManager> networkJobsQueueJobJobberManager;

  @Inject
  public NetworkJobSystem(
      @Named(GameInstanceNetworkModule.GAME_INSTANCE) Lazy<NetworkJobsQueueJobJobberManager> networkJobsQueueJobJobberManager
  ) {
    this.networkJobsQueueJobJobberManager = networkJobsQueueJobJobberManager;
  }

  @Override
  protected void processSystem() {
    networkJobsQueueJobJobberManager.get().doAllJobs();
  }
}
