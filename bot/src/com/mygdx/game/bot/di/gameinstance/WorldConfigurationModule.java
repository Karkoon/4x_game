package com.mygdx.game.bot.di.gameinstance;

import com.artemis.WorldConfiguration;
import com.mygdx.game.bot.ecs.system.BuildSystem;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.system.NetworkJobSystem;
import com.mygdx.game.client_core.ecs.system.RemovalSystem;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
@Module
public class WorldConfigurationModule {
  @Provides
  @GameInstanceScope
  public @NonNull WorldConfiguration providesWorld(
      @NonNull RemovalSystem removalSystem,
      @NonNull BuildSystem buildSystem,
      @NonNull NetworkJobSystem networkJobSystem
  ) {
    log.log(Level.INFO, "provided World");
    var configuration = new WorldConfiguration();
    configuration.setSystem(networkJobSystem);
    configuration.setSystem(buildSystem);
    configuration.setSystem(removalSystem);
    return configuration;
  }
}
