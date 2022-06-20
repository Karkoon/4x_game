package com.mygdx.game.client.di.component;

import com.mygdx.game.client.di.module.WorldConfigurationModule;
import com.mygdx.game.client.di.scope.SingleGameScope;
import com.mygdx.game.client.model.SingleGame;
import dagger.Subcomponent;

@SingleGameScope
@Subcomponent(
    modules = {
        WorldConfigurationModule.class
    }
)
public interface SingleGameComponent {
  SingleGame singleGame();

  @Subcomponent.Factory
  interface Factory {
    SingleGameComponent get();
  }
}
