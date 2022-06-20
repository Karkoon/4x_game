package com.mygdx.game.server;

import com.mygdx.game.core.di.AssetManagerModule;
import com.mygdx.game.server.di.VertxModule;
import com.mygdx.game.server.di.WorldConfigurationModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AssetManagerModule.class,
    WorldConfigurationModule.class,
    VertxModule.class
})
public interface GameFactory {
  GdxServer providesGame();
}
