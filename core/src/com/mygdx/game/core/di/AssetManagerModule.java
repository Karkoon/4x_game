package com.mygdx.game.core.di;

import com.badlogic.gdx.assets.AssetManager;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.logging.Level;

@Module
@Log
public class AssetManagerModule {
  @Provides
  @Singleton
  public @NonNull AssetManager providesAssetManager() {
    log.log(Level.INFO, "provided AssetManager");
    return new AssetManager();
  }
}
