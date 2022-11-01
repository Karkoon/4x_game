package com.mygdx.game.bot.di;

import com.mygdx.game.bot.GdxGame;
import com.mygdx.game.bot.di.gameinstance.GameScreenModule;
import com.mygdx.game.client_core.di.NetworkModule;
import com.mygdx.game.core.di.AssetManagerModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AssetManagerModule.class,
    NetworkModule.class,
    GameScreenModule.class,
    WebSocketListenerBindingsModule.class,
    MessageHandlerBindingsModule.class
})
public interface GameFactory {
  GdxGame providesGame();
}
