package com.mygdx.game.client.di.component;

import com.mygdx.game.client.di.module.NetworkModule;
import com.mygdx.game.client.di.module.StageModule;
import com.mygdx.game.client.di.scope.MenuScreenScope;
import com.mygdx.game.client.screen.MenuScreen;
import dagger.Subcomponent;

@MenuScreenScope
@Subcomponent(modules = {
    StageModule.class,
    NetworkModule.class
})
public interface MenuScreenComponent {

  MenuScreen get();

}
