package com.mygdx.game.client;

import com.mygdx.game.client.di.*;
import com.mygdx.game.client_core.di.NetworkModule;
import com.mygdx.game.core.di.AssetManagerModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    ViewportModule.class,
    WorldModule.class,
    AssetManagerModule.class,
    StageModule.class,
    NetworkModule.class,
    ComponentMessageListenerModule.class,
    NavigatorModule.class
})
public interface GameFactory {
  GdxGame providesGame();
}
