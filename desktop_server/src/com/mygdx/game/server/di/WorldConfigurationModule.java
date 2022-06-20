package com.mygdx.game.server.di;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

import javax.inject.Singleton;

@Module
@Log
public class WorldConfigurationModule {
  @Provides
  public WorldConfiguration providesWorldConfiguration() {
    return new WorldConfiguration();
  }

  @Provides
  @Singleton
  public World world(WorldConfiguration configuration) {
    return new World(configuration);
  }
}
