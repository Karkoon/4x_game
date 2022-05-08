package com.mygdx.game;

import com.mygdx.game.client.modules.*;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ViewportModule.class, EngineModule.class, AssetManagerModule.class,
    StageModule.class, TurnServiceModule.class, GameStateVerifierModule.class})
public interface GameFactory {
  MyGdxGame providesGame();
}
