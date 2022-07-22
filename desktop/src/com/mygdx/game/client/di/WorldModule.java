package com.mygdx.game.client.di;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.mygdx.game.client.ecs.system.RenderSystem;
import com.mygdx.game.client_core.ecs.system.CoordinateToPositionSystem;
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
      @NonNull RenderSystem renderSystem,
      @NonNull CoordinateToPositionSystem coordinateToPositionSystem
  ) {
    log.log(Level.INFO, "provided World");
    var configuration = new WorldConfiguration();
    configuration.setSystem(coordinateToPositionSystem);
    configuration.setSystem(renderSystem);
    return new World(configuration);
  }
}
