package com.mygdx.game.client_core.di.gameinstance;

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
  public @NonNull World providesWorld(
      WorldConfiguration worldConfiguration
  ) {
    log.log(Level.INFO, "provided World");
    return new World(worldConfiguration);
  }
}
