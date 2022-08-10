package com.mygdx.game.client.di;

import com.mygdx.game.client.network.DesktopEntityConfigHandler;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.client_core.network.comp_handlers.CoordinatesHandler;
import com.mygdx.game.client_core.network.comp_handlers.EntityConfigHandler;
import com.mygdx.game.client_core.network.comp_handlers.FieldHandler;
import com.mygdx.game.client_core.network.comp_handlers.SubFieldHandler;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.SubField;
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
      DesktopEntityConfigHandler desktopEntityConfigHandler,
      NetworkWorldEntityMapper mapper
  ) {
    var listener = new ComponentMessageListener(mapper);
    listener.registerHandler(EntityConfigId.class, entityConfigHandler);
    listener.registerHandler(EntityConfigId.class, desktopEntityConfigHandler);
    listener.registerHandler(Coordinates.class, coordinatesHandler);
    listener.registerHandler(Field.class, fieldHandler);
    listener.registerHandler(SubField.class, subFieldHandler);
    return listener;
  }
}
