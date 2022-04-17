package com.mygdx.game.client.modules;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
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
  public @NonNull Engine providesEngine() {
    log.log(Level.INFO, "provided Engine");
    return new PooledEngine();
  }
}
