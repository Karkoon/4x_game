package com.mygdx.game.server.di;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.mygdx.game.server.ecs.system.ComponentSyncSystem;
import com.mygdx.game.server.ecs.system.VisibilitySystem;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Module
@Log
public class WorldModule {
  @Provides
  @GameInstanceScope
  public @NonNull WorldConfiguration providesWorldConfiguration(
      ComponentSyncSystem componentSyncSystem,
      VisibilitySystem visibilitySystem
  ) {
    log.log(Level.INFO, "provided Server World Configuration");
    var conf =  new WorldConfiguration();
    conf.setSystem(visibilitySystem);
    conf.setSystem(componentSyncSystem);
    return conf;
  }

  @GameInstanceScope
  @Provides
  public @NonNull World providesWorld(WorldConfiguration configuration) {
    log.info("provided world");
    var world = new World(configuration);
    world.setDelta(0.5f);
    return world;
  }
}
