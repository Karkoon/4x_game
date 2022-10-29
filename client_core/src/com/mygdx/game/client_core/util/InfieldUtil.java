package com.mygdx.game.client_core.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Map;

@GameInstanceScope
@Log
public class InfieldUtil {

  private final GameConfigAssets gameConfigAssets;
  private final MaterialUtilClient materialUtilClient;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;
  private final World world;

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
    this.world = world;
    world.inject(this);
  }

  public boolean checkIfCanBuildUnit(int fieldEntityId, long unitConfigId) {
    var field = fieldMapper.get(fieldEntityId);
    var subFields = field.getSubFields();
    var unitConfig = gameConfigAssets.getGameConfigs().get(UnitConfig.class, unitConfigId);
    long requiredBuilding = unitConfig.getRequiredBuilding();

    for (int i = 0; i < subFields.size; i++) {
      int subfieldEntityId = subFields.get(i);
      subfieldEntityId = networkWorldEntityMapper.getWorldEntity(subfieldEntityId);
      var subField = subfieldMapper.get(subfieldEntityId);
      int buildingEntityId = subField.getBuilding();
      if (buildingEntityId != -0xC0FEE && !underConstructionMapper.has(buildingEntityId)) {
        var entityConfigId = entityConfigIdMapper.get(buildingEntityId);
        long buildingConfigId = entityConfigId.getId();
        if (buildingConfigId == requiredBuilding)
          return true;
      }
    }
    return false;
  }

  public boolean checkIfEnoughMaterialsToRecruitUnit(long unitConfigId) {
    var unitConfig = gameConfigAssets.getGameConfigs().get(UnitConfig.class, unitConfigId);
    Map<MaterialBase, MaterialUnit> requiredMaterials = unitConfig.getMaterials();
    return materialUtilClient.checkIfCanBuy(requiredMaterials, playerMaterialSubscriber.getEntities());
  }
}
