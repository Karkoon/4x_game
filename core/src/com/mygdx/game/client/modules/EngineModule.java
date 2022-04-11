package com.mygdx.game.client.modules;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;

import javax.inject.Singleton;

@Module
public class EngineModule {
  @Provides
  @Singleton
  public @NonNull Engine providesEngine() {
    return new PooledEngine();
  }
}
