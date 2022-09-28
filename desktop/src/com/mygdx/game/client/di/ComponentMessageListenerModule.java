package com.mygdx.game.client.di;

import com.mygdx.game.client.network.DesktopEntityConfigHandler;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.client_core.network.comp_handlers.*;
import com.mygdx.game.core.ecs.component.*;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

@Log
@Module
public class ComponentMessageListenerModule {

  @Provides
  public ComponentMessageListener provideComponentMessageListener(
      EntityConfigHandler entityConfigHandler,
      SubFieldHandler subFieldHandler,
      FieldHandler fieldHandler,
      CoordinatesHandler coordinatesHandler,
      MoveRangeHandler moveRangeHandler,
      DesktopEntityConfigHandler desktopEntityConfigHandler,
      OwnerHandler ownerHandler,
      NetworkWorldEntityMapper mapper
  ) {
    var listener = new ComponentMessageListener(mapper);
    listener.registerHandler(EntityConfigId.class, entityConfigHandler);
    listener.registerHandler(EntityConfigId.class, desktopEntityConfigHandler);
    listener.registerHandler(Coordinates.class, coordinatesHandler);
    listener.registerHandler(MoveRange.class, moveRangeHandler);
    listener.registerHandler(Field.class, fieldHandler);
    listener.registerHandler(SubField.class, subFieldHandler);
    listener.registerHandler(Owner.class, ownerHandler);
    return listener;
  }
}
