package com.mygdx.game.client.di.component;

import com.mygdx.game.client.di.module.NavigatorModule;
import com.mygdx.game.core.di.AssetManagerModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    AssetManagerModule.class,
    NavigatorModule.class
})
public interface GameComponent {

  GameScreenComponent.Factory gameScreenComponentFactory();

  LoadingScreenComponent loadingScreenComponent();

  MenuScreenComponent menuScreenComponent();
}
