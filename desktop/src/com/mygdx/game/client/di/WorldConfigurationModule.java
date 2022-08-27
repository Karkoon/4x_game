package com.mygdx.game.client.di;

import com.artemis.WorldConfiguration;
import com.mygdx.game.client.ecs.system.BlockInputSystem;
import com.mygdx.game.client.ecs.system.ChooseSystem;
import com.mygdx.game.client.ecs.system.MovementSystem;
import com.mygdx.game.client.ecs.system.NavigationSystem;
import com.mygdx.game.client.ecs.system.RenderSystem;
import com.mygdx.game.client.ecs.system.SetHighlightSystem;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.system.CoordinateToPositionSystem;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Module
@Log
public class WorldConfigurationModule {
  @Provides
  @GameInstanceScope
  public @NonNull WorldConfiguration providesWorldConfiguration(
      @NonNull ChooseSystem chooseSystem,
      @NonNull MovementSystem movementSystem,
      @NonNull RenderSystem renderSystem,
      @NonNull SetHighlightSystem setHighlightSystem,
      @NonNull CoordinateToPositionSystem coordinateToPositionSystem,
      @NonNull NavigationSystem navigationSystem,
      @NonNull BlockInputSystem blockInputSystem
  ) {
    log.log(Level.INFO, "provided World configuration");
    var configuration = new WorldConfiguration();
    configuration.setSystem(blockInputSystem);
    configuration.setSystem(chooseSystem);
    configuration.setSystem(movementSystem);
    configuration.setSystem(coordinateToPositionSystem);
    configuration.setSystem(renderSystem);
    configuration.setSystem(setHighlightSystem);
    configuration.setSystem(navigationSystem);
    return configuration;
  }
}
