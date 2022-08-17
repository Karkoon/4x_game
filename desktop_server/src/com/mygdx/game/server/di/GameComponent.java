package com.mygdx.game.server.di;

import com.mygdx.game.core.di.AssetManagerModule;
import com.mygdx.game.server.di.GeneratorBindingsModule;
import com.mygdx.game.server.GdxServer;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AssetManagerModule.class,
    GameInstanceModule.class
})
public interface GameComponent {
  GdxServer providesGame();
}
