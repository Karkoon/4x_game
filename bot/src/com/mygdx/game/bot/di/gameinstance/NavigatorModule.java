package com.mygdx.game.bot.di.gameinstance;

import com.mygdx.game.bot.screen.GameScreen;
import com.mygdx.game.bot.screen.Navigator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

@Module
@Log
public class NavigatorModule {

  @GameInstanceScope
  @Provides
  public Navigator providesNavigator(GameScreen game) {
    log.info("provided Navigator");
    return game;
  }
}
