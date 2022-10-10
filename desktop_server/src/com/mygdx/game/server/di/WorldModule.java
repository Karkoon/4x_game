package com.mygdx.game.server.di;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.mygdx.game.server.ecs.system.AddFieldOwnerIfUnitPresentSystem;
import com.mygdx.game.server.ecs.system.AddOwnerToChangeSubscribersSystem;
import com.mygdx.game.server.ecs.system.AddOwnerToSightlineSubscribersSystem;
import com.mygdx.game.server.ecs.system.ComponentSyncSystem;
import com.mygdx.game.server.ecs.system.MarkDeadEntitiesSystem;
import com.mygdx.game.server.ecs.system.RemoveDeadSystem;
import com.mygdx.game.server.ecs.system.VisibilitySystem;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
@Module
public class WorldModule {

  @Provides
  @GameInstanceScope
  public @NonNull WorldConfiguration providesWorldConfiguration(
      AddOwnerToChangeSubscribersSystem addOwnerToChangeSubscribersSystem,
      AddOwnerToSightlineSubscribersSystem addOwnerToSightlineSubscribersSystem,
      ComponentSyncSystem componentSyncSystem,
      VisibilitySystem visibilitySystem,
      MarkDeadEntitiesSystem markDeadSystem,
      RemoveDeadSystem removeDeadSystem,
      AddFieldOwnerIfUnitPresentSystem addFieldOwnerIfUnitPresentSystem
  ) {
    log.log(Level.INFO, "provided Server World Configuration");
    var conf =  new WorldConfiguration();
    conf.setSystem(addFieldOwnerIfUnitPresentSystem);
    conf.setSystem(addOwnerToChangeSubscribersSystem);
    conf.setSystem(addOwnerToSightlineSubscribersSystem);
    conf.setSystem(visibilitySystem);
    conf.setSystem(markDeadSystem);
    conf.setSystem(componentSyncSystem);
    conf.setSystem(removeDeadSystem);
    return conf;
  }

  @GameInstanceScope
  @Provides
  public @NonNull World providesWorld(WorldConfiguration configuration) {
    log.info("provided world");
    var world = new World(configuration);
    world.setDelta(0.5f);
    return world;
  }
}
