package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.Component;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client_core.ecs.entityfactory.FieldFactory;
import com.mygdx.game.client_core.ecs.entityfactory.SubFieldFactory;
import com.mygdx.game.client_core.ecs.entityfactory.UnitFactory;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;
import static com.github.czyzby.websocket.WebSocketListener.NOT_HANDLED;

@Log
public class EntityConfigHandler implements ComponentMessageListener.Handler {

  private final GameConfigAssets assets;
  private final FieldFactory fieldFactory;
  private final UnitFactory unitFactory;
  private final SubFieldFactory subFieldFactory;

  @Inject
  public EntityConfigHandler(
      GameConfigAssets assets,
      FieldFactory fieldFactory,
      UnitFactory unitFactory,
      SubFieldFactory subFieldFactory
  ) {
    this.assets = assets;
    this.fieldFactory = fieldFactory;
    this.unitFactory = unitFactory;
    this.subFieldFactory = subFieldFactory;
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    var entityConfigId = ((EntityConfigId) component).getId();
    if (entityConfigId >= GameConfigs.FIELD_MIN && entityConfigId < GameConfigs.FIELD_MAX) {
      log.info("field id " + worldEntity);
      var config = assets.getGameConfigs().get(FieldConfig.class, entityConfigId);
      fieldFactory.createEntity(config, worldEntity);
      return FULLY_HANDLED;
    } else if (entityConfigId >= GameConfigs.UNIT_MIN && entityConfigId < GameConfigs.UNIT_MAX) {
      log.info("unit id " + worldEntity);
      var config = assets.getGameConfigs().get(UnitConfig.class, entityConfigId);
      unitFactory.createEntity(config, worldEntity);
      return FULLY_HANDLED;
    } else if (entityConfigId >= GameConfigs.SUBFIELD_MIN && entityConfigId < GameConfigs.SUBFIELD_MAX) {
      log.info("subfield id " + worldEntity);
      var config = assets.getGameConfigs().get(SubFieldConfig.class, entityConfigId);
      subFieldFactory.createEntity(config, worldEntity);
      return FULLY_HANDLED;
    }
    return NOT_HANDLED;
  }
}
