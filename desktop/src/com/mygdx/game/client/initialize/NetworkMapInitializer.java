package com.mygdx.game.client.initialize;

import com.mygdx.game.core.initialize.MapInitializer;
import com.mygdx.game.core.model.Coordinates;
import com.mygdx.game.core.network.ServerConnection;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import lombok.NonNull;

import java.util.Map;
// TODO: 04.06.2022 use it for rpc calls??
public class NetworkMapInitializer implements MapInitializer {

  private final ServerConnection connection;

  @AssistedInject
  public NetworkMapInitializer(@NonNull @Assisted ServerConnection connection) {
    this.connection = connection;
  }

  @Override
  public Map<Coordinates, Integer> initializeMap() {
    return null/*connection.getMap()*/; // idk how, maybe the connection.getMap could be implemented here
  }

  @AssistedFactory
  public interface NetworkMapInitializerFactory {
    NetworkMapInitializer get(ServerConnection connection);
  }
}
