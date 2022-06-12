package com.mygdx.game.server;

import com.mygdx.game.core.di.AssetManagerModule;
import com.mygdx.game.core.di.CoreComponentMapperModule;
import com.mygdx.game.server.di.InitializerModule;
import com.mygdx.game.server.di.WorldModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AssetManagerModule.class,
    CoreComponentMapperModule.class,
    WorldModule.class,
    InitializerModule.class
})
public interface GameFactory {
  GdxServer providesGame();
}
