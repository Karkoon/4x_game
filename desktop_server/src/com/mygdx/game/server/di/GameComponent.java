package com.mygdx.game.server.di;

import com.mygdx.game.core.di.AssetManagerModule;
import com.mygdx.game.server.GdxServer;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AssetManagerModule.class,
    GeneratorBindingsModule.class,
    WorldModule.class
})
public interface GameComponent {
  GdxServer providesGame();
}
