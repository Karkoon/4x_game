package com.mygdx.game.client.di;

import dagger.Subcomponent;

@Subcomponent
public interface GameScreenSubcomponent {

  @Subcomponent.Builder
  interface Builder {
    GameScreenSubcomponent get();
  }
}
