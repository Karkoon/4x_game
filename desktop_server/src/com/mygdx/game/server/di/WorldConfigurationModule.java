package com.mygdx.game.server.di;

import com.artemis.WorldConfiguration;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.logging.Level;

@Module
@Log
@GameInstanceScope
public class WorldConfigurationModule {
  @Provides
  public @NonNull WorldConfiguration providesWorld(
      /* nothing to process lmao */
  ) {
    log.log(Level.INFO, "provided Server World Configuration");
    return new WorldConfiguration();
  }
}
