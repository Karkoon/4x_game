package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import com.mygdx.game.server.model.Client;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class CreateUnitService extends WorldService {

  private final UnitFactory unitFactory;
  private final GameConfigAssets assets;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<SubField> subfieldsMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;

  @Inject
  public CreateUnitService(
      UnitFactory unitFactory,
      GameConfigAssets assets,
      World world
  ) {
    this.unitFactory = unitFactory;
    this.assets = assets;
    world.inject(this);
  }

  public void createUnit(int unitConfigId, int fieldEntityId, Client client, boolean skipChecking) {
    var subFields = fieldMapper.get(fieldEntityId).getSubFields();
    var unitConfig = assets.getGameConfigs().get(UnitConfig.class, unitConfigId);
    long requiredBuilding = unitConfig.getRequiredBuilding();
    boolean canCreate = skipChecking;
    for (int i = 0; i < subFields.size; i++) {
      var subField = subfieldsMapper.get(subFields.get(i));
      int buildingEntityId = subField.getBuilding();
      if (buildingEntityId != -0xC0FEE && !underConstructionMapper.has(buildingEntityId)) {
        var entityConfigId = entityConfigIdMapper.get(buildingEntityId);
        long buildingConfigId = entityConfigId.getId();
        if (buildingConfigId == requiredBuilding)
          canCreate = true;
      }
    }

    if (canCreate) {
      var config = assets.getGameConfigs().get(UnitConfig.class, unitConfigId);
      var coordinates = coordinatesMapper.get(fieldEntityId);
      unitFactory.createEntity(config, coordinates, client);
    } else {
      log.info("Field " + fieldEntityId + " don't have enough buildings to create " + unitConfigId);
    }
  }
}
