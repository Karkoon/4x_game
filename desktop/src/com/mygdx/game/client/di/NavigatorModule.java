package com.mygdx.game.client.di;

import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.screen.Navigator;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

import javax.inject.Singleton;

@Module
@Log
public class NavigatorModule {

  @Singleton
  @Provides
  public Navigator providesNavigator(GdxGame game) {
    log.info("provided Navigator");
    return game;
  }
}
