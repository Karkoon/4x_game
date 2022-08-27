package com.mygdx.game.client.di;

import com.mygdx.game.client.screen.GameScreen;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.di.gameinstance.WorldModule;
import dagger.Subcomponent;

@Subcomponent(
    modules = {
        WorldModule.class,
        NavigatorModule.class,
        WorldConfigurationModule.class
    }
)
@GameInstanceScope
public interface GameScreenSubcomponent {
  GameScreen get();

  @Subcomponent.Builder
  interface Builder {
    GameScreenSubcomponent build();
  }
}
