package com.mygdx.game.server;

import com.mygdx.game.core.di.AssetManagerModule;
import com.mygdx.game.server.di.GeneratorBindingsModule;
import com.mygdx.game.server.di.WorldModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AssetManagerModule.class,
    GeneratorBindingsModule.class,
    WorldModule.class
})
public interface GameFactory {
  GdxServer providesGame();
}
