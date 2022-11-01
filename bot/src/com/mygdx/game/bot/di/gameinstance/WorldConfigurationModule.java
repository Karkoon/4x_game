package com.mygdx.game.bot.di.gameinstance;

import com.artemis.WorldConfiguration;
import com.mygdx.game.bot.ecs.system.BuildSystem;
import com.mygdx.game.bot.ecs.system.ChooseSystem;
import com.mygdx.game.bot.ecs.system.CoordinateToPositionSystem;
import com.mygdx.game.bot.ecs.system.MovementSystem;
import com.mygdx.game.bot.ecs.system.RenderSystem;
import com.mygdx.game.bot.ecs.system.SelfVisibilitySystem;
import com.mygdx.game.bot.ecs.system.SetHighlightSystem;
import com.mygdx.game.bot.ecs.system.VisibilitySystem;
import com.mygdx.game.bot.ecs.system.AttackSystem;
import com.mygdx.game.bot.ecs.system.BlockInputSystem;
import com.mygdx.game.bot.ecs.system.NavigationSystem;
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
      @NonNull CoordinateToPositionSystem coordinateToPositionSystem,
      @NonNull ChooseSystem chooseSystem,
      @NonNull MovementSystem movementSystem,
      @NonNull BuildSystem buildSystem,
      @NonNull RenderSystem renderSystem,
      @NonNull SetHighlightSystem setHighlightSystem,
      @NonNull NavigationSystem navigationSystem,
      @NonNull BlockInputSystem blockInputSystem,
      @NonNull VisibilitySystem visibilitySystem,
      @NonNull SelfVisibilitySystem selfVisibilitySystem,
      @NonNull AttackSystem attackSystem,
      @NonNull NetworkJobSystem networkJobSystem
  ) {
    log.log(Level.INFO, "provided World");
    var configuration = new WorldConfiguration();
    configuration.setSystem(networkJobSystem);
    configuration.setSystem(coordinateToPositionSystem);
    configuration.setSystem(visibilitySystem);
    configuration.setSystem(selfVisibilitySystem);
    configuration.setSystem(blockInputSystem);
    configuration.setSystem(chooseSystem);
    configuration.setSystem(movementSystem);
    configuration.setSystem(buildSystem);
    configuration.setSystem(renderSystem);
    configuration.setSystem(attackSystem);
    configuration.setSystem(setHighlightSystem);
    configuration.setSystem(navigationSystem);
    configuration.setSystem(removalSystem);
    return configuration;
  }
}
