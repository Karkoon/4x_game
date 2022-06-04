package com.mygdx.game.client.initialize;

import com.mygdx.game.core.initialize.StartUnitInitializer;
import com.mygdx.game.core.network.ServerConnection;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import lombok.NonNull;

// TODO: 04.06.2022 use it for rpc calls??
public class NetworkStartUnitInitializer implements StartUnitInitializer {

  private final ServerConnection connection;

  @AssistedInject
  public NetworkStartUnitInitializer(@Assisted @NonNull ServerConnection connection) {
    this.connection = connection;
  }


  @Override
  public void initializeTestUnit(int field) {

  }

  @AssistedFactory
  public interface NetworkStartUnitInitializerFactory {
    NetworkStartUnitInitializer get(ServerConnection connection);
  }
}
