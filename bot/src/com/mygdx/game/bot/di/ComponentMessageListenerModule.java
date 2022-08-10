package com.mygdx.game.bot.di;

import com.mygdx.game.bot.network.comp_handlers.RemoveFromMapHandler;
import com.mygdx.game.bot.network.comp_handlers.SaveToMapHandler;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.client_core.network.comp_handlers.CoordinatesHandler;
import com.mygdx.game.client_core.network.comp_handlers.EntityConfigHandler;
import com.mygdx.game.client_core.network.comp_handlers.FieldHandler;
import com.mygdx.game.client_core.network.comp_handlers.PlayerTokenHandler;
import com.mygdx.game.client_core.network.comp_handlers.SubFieldHandler;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.PlayerToken;
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
      NetworkWorldEntityMapper mapper,
      RemoveFromMapHandler removeFromMapHandler,
      SaveToMapHandler saveToMapHandler,
      PlayerTokenHandler playerTokenHandler
  ) {
    var listener = new ComponentMessageListener(mapper);
    listener.registerHandler(EntityConfigId.class, entityConfigHandler);
    listener.registerHandler(Coordinates.class, removeFromMapHandler);
    listener.registerHandler(Coordinates.class, coordinatesHandler);
    listener.registerHandler(Coordinates.class, saveToMapHandler);
    listener.registerHandler(Field.class, fieldHandler);
    listener.registerHandler(SubField.class, subFieldHandler);
    listener.registerHandler(PlayerToken.class, playerTokenHandler);
    return listener;
  }
}
