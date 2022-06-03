package com.mygdx.game.client.modules;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.mygdx.game.client.ecs.system.RenderSystem;
import com.mygdx.game.client.ecs.system.UnitMovementSystem;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.logging.Level;

@Module
@Log
public class EngineModule {
  @Provides
  @Singleton
  public @NonNull Engine providesEngine(@NonNull RenderSystem renderSystem,
                                        @NonNull UnitMovementSystem unitMovementSystem) {
    log.log(Level.INFO, "provided Engine");
    var engine = new PooledEngine();
    engine.addSystem(renderSystem);
    engine.addSystem(unitMovementSystem);
    return engine;
  }
}
