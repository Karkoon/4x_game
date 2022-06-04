package com.mygdx.game.client.di;

import com.mygdx.game.core.initialize.MapInitializer;
import com.mygdx.game.core.initialize.StartUnitInitializer;
import com.mygdx.game.core.network.ServerConnection;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

import static com.mygdx.game.client.initialize.NetworkMapInitializer.NetworkMapInitializerFactory;
import static com.mygdx.game.client.initialize.NetworkStartUnitInitializer.NetworkStartUnitInitializerFactory;

@Module
public class InitializerModule {
  @Provides
  @Reusable
  public MapInitializer mapInitializer(
      NetworkMapInitializerFactory factory,
      ServerConnection connection
  ) {
    return factory.get(connection);
  }

  @Provides
  @Reusable
  public StartUnitInitializer startUnitInitializer(
      NetworkStartUnitInitializerFactory factory,
      ServerConnection connection
  ) {
    return factory.get(connection);
  }
}
