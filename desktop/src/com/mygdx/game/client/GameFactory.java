package com.mygdx.game.client;

import com.mygdx.game.client.di.*;
import com.mygdx.game.core.di.AssetManagerModule;
import com.mygdx.game.core.di.CoreComponentMapperModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    ViewportModule.class,
    WorldModule.class,
    AssetManagerModule.class,
    StageModule.class,
    InitializerModule.class,
    CoreComponentMapperModule.class,
    NetworkModule.class
})
public interface GameFactory {
  GdxGame providesGame();
}
