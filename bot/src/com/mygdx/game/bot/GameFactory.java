package com.mygdx.game.bot;

import com.mygdx.game.bot.di.ComponentMessageListenerModule;
import com.mygdx.game.bot.di.WorldModule;
import com.mygdx.game.client_core.di.MainSchedulerModule;
import com.mygdx.game.client_core.di.NetworkModule;
import com.mygdx.game.core.di.AssetManagerModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    WorldModule.class,
    AssetManagerModule.class,
    NetworkModule.class,
    ComponentMessageListenerModule.class,
    MainSchedulerModule.class
})
public interface GameFactory {
  GdxGame providesGame();
}
