package com.mygdx.game.bot.di.gameinstance;

import com.mygdx.game.bot.screen.GameScreen;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceNetworkModule;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.di.gameinstance.WorldModule;
import dagger.Subcomponent;

@Subcomponent(
    modules = {
        WorldModule.class,
        WorldConfigurationModule.class,
        ComponentMessageListenerModule.class,
        MessageHandlerBindingsModule.class,
        GameInstanceNetworkModule.class,
        GameInstanceWebSocketListenerBindingsModule.class
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
