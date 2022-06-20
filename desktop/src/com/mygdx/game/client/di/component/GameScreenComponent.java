package com.mygdx.game.client.di.component;

import com.mygdx.game.client.di.module.NetworkModule;
import com.mygdx.game.client.di.module.StageModule;
import com.mygdx.game.client.di.module.ViewportModule;
import com.mygdx.game.client.di.scope.GameScreenScope;
import com.mygdx.game.client.screen.GameScreen;
import dagger.Subcomponent;

@GameScreenScope
@Subcomponent(modules = {
    ViewportModule.class,
    StageModule.class,
    NetworkModule.class
})
public interface GameScreenComponent {

  GameScreen gameScreen();

  SingleGameComponent.Factory singleGameComponentFactory();

  @Subcomponent.Factory
  interface Factory {
    GameScreenComponent setNetworkModule(NetworkModule networkModule);
  }
}
