package com.mygdx.game.client.di.module;

import com.mygdx.game.client.Navigator;
import dagger.Module;
import dagger.Provides;

@Module
public class NavigatorModule {

  private final Navigator navigator;

  public NavigatorModule(Navigator navigator) {
    this.navigator = navigator;
  }

  @Provides
  public Navigator navigator() {
    return navigator;
  }
}
