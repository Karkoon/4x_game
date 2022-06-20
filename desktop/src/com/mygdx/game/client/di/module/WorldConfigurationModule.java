package com.mygdx.game.client.di.module;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.mygdx.game.client.di.scope.SingleGameScope;
import com.mygdx.game.client.ecs.system.CoordinateToPositionSystem;
import com.mygdx.game.client.ecs.system.RenderSystem;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

@Module
@Log
public class WorldConfigurationModule {
  @Provides
  @SingleGameScope
  public WorldConfiguration providesWorldConfiguration(
      @NonNull RenderSystem renderSystem,
      @NonNull CoordinateToPositionSystem coordinateToPositionSystem
  ) {
    var configuration = new WorldConfiguration();
    configuration.expectedEntityCount(500);
    configuration.setSystem(coordinateToPositionSystem);
    configuration.setSystem(renderSystem);
    return configuration;
  }

  @Provides
  @SingleGameScope
  public World world(WorldConfiguration configuration) {
    return new World(configuration);
  }
}
