package com.mygdx.game.server.di;

import com.artemis.World;
import com.artemis.WorldConfiguration;
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
      /* nothing to process lmao */
  ) {
    log.log(Level.INFO, "provided Server World Configuration");
    return new WorldConfiguration();
  }

  @GameInstanceScope
  @Provides
  public @NonNull World providesWorld(WorldConfiguration configuration) {
    return new World(configuration);
  }
}
