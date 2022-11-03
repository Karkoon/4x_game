package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.InRecruitment;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.model.BuildingImpactParameter;
import com.mygdx.game.core.model.BuildingImpactValue;
import com.mygdx.game.core.model.BuildingType;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.util.MaterialUtilServer;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class CreateUnitService extends WorldService {

  private final ComponentFactory componentFactory;
  private final GameConfigAssets assets;
  private final MaterialUtilServer materialUtilServer;
  private final UnitFactory unitFactory;
  private final World world;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<InRecruitment> inRecruitmentMapper;
  private ComponentMapper<SubField> subfieldsMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;

  @Inject
  public CreateUnitService(
      ComponentFactory componentFactory,
      GameConfigAssets assets,
      MaterialUtilServer materialUtilServer,
      UnitFactory unitFactory,
      World world
  ) {
    this.componentFactory = componentFactory;
    this.assets = assets;
    this.materialUtilServer = materialUtilServer;
    this.unitFactory = unitFactory;
    this.world = world;
    world.inject(this);
  }

  public void createUnit(int unitConfigId, int fieldEntityId, Client client, boolean skipChecking) {
    var subFields = fieldMapper.get(fieldEntityId).getSubFields();
    var unitConfig = assets.getGameConfigs().get(UnitConfig.class, unitConfigId);
    var requiredMaterials = unitConfig.getMaterials();
    boolean canCreate = skipChecking;
    boolean enoughtMaterials = materialUtilServer.checkIfCanBuy(client.getPlayerToken(), requiredMaterials);
    for (int i = 0; i < subFields.size && !canCreate; i++) {
      var subField = subfieldsMapper.get(subFields.get(i));
      int buildingEntityId = subField.getBuilding();
      if (buildingEntityId != -0xC0FEE && !underConstructionMapper.has(buildingEntityId)) {
        var entityConfigId = entityConfigIdMapper.get(buildingEntityId);
        long buildingConfigId = entityConfigId.getId();
        var buildingConfig = assets.getGameConfigs().get(BuildingConfig.class, buildingConfigId);
        if (buildingConfig.getImpact().getBuildingType() == BuildingType.RECRUITMENT_BUILDING) {
          for (BuildingImpactValue buildingImpactValue : buildingConfig.getImpact().getBuildingImpactValues()) {
            if (buildingImpactValue.getParameter() == BuildingImpactParameter.RECRUIT &&
                buildingImpactValue.getValue() == unitConfigId) {
              canCreate = true;
              break;
            }
          }
        }
      }
    }

    if (canCreate) {
      var config = assets.getGameConfigs().get(UnitConfig.class, unitConfigId);
      if (skipChecking) {
        var coordinates = coordinatesMapper.get(fieldEntityId);
        unitFactory.createEntity(config, coordinates, client);
      } else if (enoughtMaterials){
        materialUtilServer.removeMaterials(client.getPlayerToken(), requiredMaterials);
        String playerToken = client.getPlayerToken();
        componentFactory.createInRecruitmentComponent(fieldEntityId, config.getTurnAmount(), config.getId(), playerToken);
        setDirty(fieldEntityId, InRecruitment.class, world);
      } else {
        log.info("Not enought materials to create " + unitConfigId);
      }
    } else {
      log.info("Field " + fieldEntityId + " don't have enough buildings to create " + unitConfigId);
    }
  }
}
