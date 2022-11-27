package com.mygdx.game.client_core.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.model.BuildingImpactParameter;
import com.mygdx.game.core.model.BuildingImpactValue;
import com.mygdx.game.core.model.BuildingType;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class InfieldUtil {

  private final GameConfigAssets gameConfigAssets;
  private final MaterialUtilClient materialUtilClient;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @AspectDescriptor(all = {PlayerMaterial.class})
  private EntitySubscription playerMaterialSubscriber;

  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<SubField> subfieldMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;

  @Inject
  public InfieldUtil(
      GameConfigAssets gameConfigAssets,
      MaterialUtilClient materialUtilClient,
      NetworkWorldEntityMapper networkWorldEntityMapper,
      World world
  ) {
    this.gameConfigAssets = gameConfigAssets;
    this.materialUtilClient = materialUtilClient;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    world.inject(this);
  }

  public boolean checkIfCanBuildUnit(int fieldEntityId, long unitConfigId) {
    var field = fieldMapper.get(fieldEntityId);
    var subFields = field.getSubFields();
    for (int i = 0; i < subFields.size; i++) {
      int subfieldEntityId = subFields.get(i);
      subfieldEntityId = networkWorldEntityMapper.getWorldEntity(subfieldEntityId);
      var subField = subfieldMapper.get(subfieldEntityId);
      int buildingEntityId = subField.getBuilding();
      if (buildingEntityId != -0xC0FEE && !underConstructionMapper.has(buildingEntityId)) {
        var entityConfigId = entityConfigIdMapper.get(buildingEntityId);
        long buildingConfigId = entityConfigId.getId();        var buildingConfig = gameConfigAssets.getGameConfigs().get(BuildingConfig.class, buildingConfigId);

        if (buildingConfig.getImpact().getBuildingType() == BuildingType.RECRUITMENT_BUILDING) {
          for (BuildingImpactValue buildingImpactValue : buildingConfig.getImpact().getBuildingImpactValues()) {
            if (buildingImpactValue.getParameter() == BuildingImpactParameter.RECRUIT &&
                    buildingImpactValue.getValue() == unitConfigId) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public boolean checkIfEnoughMaterialsToRecruitUnit(long unitConfigId) {
    var unitConfig = gameConfigAssets.getGameConfigs().get(UnitConfig.class, unitConfigId);
    var requiredMaterials = unitConfig.getMaterials();
    return materialUtilClient.checkIfCanBuy(requiredMaterials, playerMaterialSubscriber.getEntities());
  }
}
