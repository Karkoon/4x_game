package com.mygdx.game.client.di;

import com.mygdx.game.client.ecs.entityfactory.ChooseableSetter;
import com.mygdx.game.client.ecs.entityfactory.DesktopCoordinateSetter;
import com.mygdx.game.client.ecs.entityfactory.ModelInstanceCompSetter;
import com.mygdx.game.client.ecs.entityfactory.NavigationDirectionSetter;
import com.mygdx.game.client.ecs.entityfactory.TextureCompSetter;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public interface SetterBindingsModule {
  @Binds
  @IntoSet
  Setter providesChooseableSetter(ChooseableSetter setter);

  @Binds
  @IntoSet
  Setter providesDesktopCoordinateSetter(DesktopCoordinateSetter setter);

  @Binds
  @IntoSet
  Setter providesModelInstanceCompSetter(ModelInstanceCompSetter setter);

  @Binds
  @IntoSet
  Setter providesNavigationDirectionSetter(NavigationDirectionSetter setter);

  @Binds
  @IntoSet
  Setter providesTextureCompSetter(TextureCompSetter setter);
}
