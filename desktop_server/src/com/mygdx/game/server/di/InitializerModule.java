package com.mygdx.game.server.di;

import com.mygdx.game.core.initialize.MapInitializer;
import com.mygdx.game.core.initialize.StartUnitInitializer;
import com.mygdx.game.server.initialize.LocalMapInitializer;
import com.mygdx.game.server.initialize.LocalStartUnitInitializer;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public class InitializerModule {
  @Provides
  @Reusable
  public MapInitializer mapInitializer(LocalMapInitializer mapInitializer) {
    return mapInitializer;
  }

  @Provides
  @Reusable
  public StartUnitInitializer startUnitInitializer(LocalStartUnitInitializer startUnitInitializer) {
    return startUnitInitializer;
  }
}
