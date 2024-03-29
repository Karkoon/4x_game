package com.mygdx.game.client_core.network.comp_handlers;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.entityfactory.BuildingFactory;
import com.mygdx.game.client_core.ecs.entityfactory.FieldFactory;
import com.mygdx.game.client_core.ecs.entityfactory.SubFieldFactory;
import com.mygdx.game.client_core.ecs.entityfactory.TechnologyFactory;
import com.mygdx.game.client_core.ecs.entityfactory.UnitFactory;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;
import static com.github.czyzby.websocket.WebSocketListener.NOT_HANDLED;

@GameInstanceScope

@Log
public class EntityConfigHandler implements ComponentMessageListener.Handler<EntityConfigId> {

  private final BuildingFactory buildingFactory;
  private final FieldFactory fieldFactory;
  private final GameConfigAssets gameConfigAssets;
  private final SubFieldFactory subFieldFactory;
  private final TechnologyFactory technologyFactory;
  private final UnitFactory unitFactory;

  @Inject
  public EntityConfigHandler(
      BuildingFactory buildingFactory,
      FieldFactory fieldFactory,
      GameConfigAssets gameConfigAssets,
      SubFieldFactory subFieldFactory,
      TechnologyFactory technologyFactory,
      UnitFactory unitFactory
  ) {
    this.buildingFactory = buildingFactory;
    this.fieldFactory = fieldFactory;
    this.gameConfigAssets = gameConfigAssets;
    this.subFieldFactory = subFieldFactory;
    this.technologyFactory = technologyFactory;
    this.unitFactory = unitFactory;
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, EntityConfigId component) {
    var entityConfigId = component.getId();
    if (entityConfigId >= GameConfigs.FIELD_MIN && entityConfigId <= GameConfigs.FIELD_MAX) {
      log.info("field id " + worldEntity);
      var config = gameConfigAssets.getGameConfigs().get(FieldConfig.class, entityConfigId);
      fieldFactory.createEntity(config, worldEntity);
      return FULLY_HANDLED;
    } else if (entityConfigId >= GameConfigs.UNIT_MIN && entityConfigId <= GameConfigs.UNIT_MAX) {
      log.info("unit id " + worldEntity);
      var config = gameConfigAssets.getGameConfigs().get(UnitConfig.class, entityConfigId);
      unitFactory.createEntity(config, worldEntity);
      return FULLY_HANDLED;
    } else if (entityConfigId >= GameConfigs.SUBFIELD_MIN && entityConfigId <= GameConfigs.SUBFIELD_MAX) {
      log.info("subfield id " + worldEntity);
      var config = gameConfigAssets.getGameConfigs().get(SubFieldConfig.class, entityConfigId);
      subFieldFactory.createEntity(config, worldEntity);
      return FULLY_HANDLED;
    } else if (entityConfigId >= GameConfigs.TECHNOLOGY_MIN && entityConfigId <= GameConfigs.TECHNOLOGY_MAX) {
      log.info("technology id " + worldEntity);
      var config = gameConfigAssets.getGameConfigs().get(TechnologyConfig.class, entityConfigId);
      technologyFactory.createEntity(config, worldEntity);
      return FULLY_HANDLED;
    } else if (entityConfigId >= GameConfigs.BUILDING_MIN && entityConfigId <= GameConfigs.BUILDING_MAX) {
      log.info("building id " + worldEntity);
      var config = gameConfigAssets.getGameConfigs().get(BuildingConfig.class, entityConfigId);
      buildingFactory.createEntity(config, worldEntity);
      return FULLY_HANDLED;
    }
    return NOT_HANDLED;
  }
}
