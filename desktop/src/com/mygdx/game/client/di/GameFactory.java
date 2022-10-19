package com.mygdx.game.client.di;

import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.di.gameinstance.GameScreenModule;
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
    GameScreenModule.class,
    WebSocketListenerBindingsModule.class,
    MessageHandlerBindingsModule.class
})
public interface GameFactory {
  GdxGame providesGame();
}
