package com.mygdx.game.bot.di.gameinstance;

import com.mygdx.game.bot.ecs.entityfactory.DesktopCoordinateSetter;
import com.mygdx.game.bot.ecs.entityfactory.ModelInstanceCompSetter;
import com.mygdx.game.bot.ecs.entityfactory.ChoosableSetter;
import com.mygdx.game.bot.ecs.entityfactory.NavigationDirectionSetter;
import com.mygdx.game.bot.ecs.entityfactory.TextureCompSetter;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public interface SetterBindingsModule {

  @Binds
  @IntoSet
  @GameInstanceScope
  Setter providesChoosableSetter(ChoosableSetter setter);

  @Binds
  @IntoSet
  @GameInstanceScope
  Setter providesDesktopCoordinateSetter(DesktopCoordinateSetter setter);

  @Binds
  @IntoSet
  @GameInstanceScope
  Setter providesModelInstanceCompSetter(ModelInstanceCompSetter setter);

  @Binds
  @IntoSet
  @GameInstanceScope
  Setter providesNavigationDirectionSetter(NavigationDirectionSetter setter);

  @Binds
  @IntoSet
  @GameInstanceScope
  Setter providesTextureCompSetter(TextureCompSetter setter);
}
