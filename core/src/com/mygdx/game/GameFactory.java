package com.mygdx.game;

import com.mygdx.game.client.modules.AssetManagerModule;
import com.mygdx.game.client.modules.EngineModule;
import com.mygdx.game.client.modules.ViewportModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ViewportModule.class, EngineModule.class, AssetManagerModule.class})
public interface GameFactory {
  MyGdxGame providesGame();
}

