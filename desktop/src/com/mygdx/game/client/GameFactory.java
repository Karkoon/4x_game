package com.mygdx.game.client;

import com.mygdx.game.client.di.ComponentMessageListenerModule;
import com.mygdx.game.client.di.GameScreenModule;
import com.mygdx.game.client.di.SetterBindingsModule;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.di.ViewportModule;
import com.mygdx.game.client_core.di.NetworkModule;
import com.mygdx.game.core.di.AssetManagerModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    ViewportModule.class,
    AssetManagerModule.class,
    StageModule.class,
    NetworkModule.class,
    ComponentMessageListenerModule.class,
    SetterBindingsModule.class,
    GameScreenModule.class
})
public interface GameFactory {
  GdxGame providesGame();
}
