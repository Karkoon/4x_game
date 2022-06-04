package com.mygdx.game.server;

import com.mygdx.game.core.di.AssetManagerModule;
import com.mygdx.game.core.di.CoreComponentMapperModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AssetManagerModule.class, CoreComponentMapperModule.class})
public interface GameFactory {
  MyGdxGame providesGame();
}
