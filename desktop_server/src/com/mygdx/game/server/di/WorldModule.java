package com.mygdx.game.server.di;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.mygdx.game.server.ecs.system.SlotPositionSystem;
import com.mygdx.game.server.ecs.system.UnitMovementSystem;
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
      @NonNull UnitMovementSystem movementSystem,
      @NonNull SlotPositionSystem slotPositionSystem
  ) {
    log.log(Level.INFO, "provided Client World");
    var worldConfiguration = new WorldConfiguration();
    worldConfiguration.setSystem(movementSystem);
    worldConfiguration.setSystem(slotPositionSystem);
    return new World(worldConfiguration);
  }
}
