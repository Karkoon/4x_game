package com.mygdx.game.server.di;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.logging.Level;

@Module
@Log
public class WorldModule {
  @Provides
  @Singleton
  public @NonNull World providesWorld(
      /* nothing to process lmao */
  ) {
    log.log(Level.INFO, "provided Server World");
    var worldConfiguration = new WorldConfiguration();
    return new World(worldConfiguration);
  }
}
