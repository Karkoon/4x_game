package com.mygdx.game;

import com.mygdx.game.client.modules.EngineModule;
import com.mygdx.game.client.modules.ViewportModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ViewportModule.class, EngineModule.class})
public interface GameComponent {
    MyGdxGame providesGame();
}

