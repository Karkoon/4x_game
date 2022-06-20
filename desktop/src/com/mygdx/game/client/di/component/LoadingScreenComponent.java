package com.mygdx.game.client.di.component;

import com.mygdx.game.client.di.module.StageModule;
import com.mygdx.game.client.di.scope.LoadingScreenScope;
import com.mygdx.game.client.screen.LoadingScreen;
import dagger.Subcomponent;

@LoadingScreenScope
@Subcomponent(modules = {
    StageModule.class,
})
public interface LoadingScreenComponent {

  LoadingScreen get();

}
