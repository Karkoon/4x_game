package com.mygdx.game.client.di;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.mygdx.game.client.ecs.system.AttackSystem;
import com.mygdx.game.client.ecs.system.BlockInputSystem;
import com.mygdx.game.client.ecs.system.BuildSystem;
import com.mygdx.game.client.ecs.system.ChooseSystem;
import com.mygdx.game.client.ecs.system.MovementSystem;
import com.mygdx.game.client.ecs.system.NavigationSystem;
import com.mygdx.game.client.ecs.system.RenderSystem;
import com.mygdx.game.client.ecs.system.SelfVisibilitySystem;
import com.mygdx.game.client.ecs.system.SetHighlightSystem;
import com.mygdx.game.client.ecs.system.VisibilitySystem;
import com.mygdx.game.client_core.ecs.system.CoordinateToPositionSystem;
import com.mygdx.game.client_core.ecs.system.NetworkJobSystem;
import com.mygdx.game.client_core.ecs.system.RemovalSystem;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.logging.Level;

@Log
@Module
public class WorldModule {
  @Provides
  @Singleton
  public @NonNull World providesWorld(
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
    return new World(configuration);
  }
}
