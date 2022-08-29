package com.mygdx.game.client.di;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ModelRenderingModule {
  @Provides
  @Singleton
  public ModelCache providesModelCache() {
    return new ModelCache();
  }

  @Provides
  @Singleton
  public ModelBatch providesModelBatch() {
    return new ModelBatch();
  }
}
