package com.mygdx.game.server.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.MaterialIncome;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.ecs.component.Researched;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.Technology;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.model.BuildingImpactValue;
import com.mygdx.game.core.model.BuildingType;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import com.mygdx.game.core.model.TechnologyImpactType;
import com.mygdx.game.core.model.TechnologyImpactValue;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.network.gameinstance.services.WorldService;

import javax.inject.Inject;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@GameInstanceScope
public class MaterialUtilServer extends WorldService {

  private final GameConfigAssets gameConfigAssets;
  private final World world;

  @AspectDescriptor(all = {Owner.class, Field.class})
  private EntitySubscription ownerFieldsSubscriber;

  @AspectDescriptor(all = {Owner.class, PlayerMaterial.class})
  private EntitySubscription ownerPlayerMaterialSubscriber;

  @AspectDescriptor(all = {Researched.class, Owner.class})
  private EntitySubscription researchedOwnerSubscriber;

  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<MaterialIncome> materialIncomeMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<PlayerMaterial> playerMaterialMapper;
  private ComponentMapper<SubField> subfieldMapper;
  private ComponentMapper<Technology> technologyMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;

  @Inject
  MaterialUtilServer(
      GameConfigAssets gameConfigAssets,
      World world
  ) {
    this.gameConfigAssets = gameConfigAssets;
    world.inject(this);
    this.world = world;
  }

  public boolean checkIfCanBuy(String playerToken, Map<MaterialBase, MaterialUnit> materials) {
    for (int i = 0; i < ownerPlayerMaterialSubscriber.getEntities().size(); i++) {
      int entityId = ownerPlayerMaterialSubscriber.getEntities().get(i);
      if (!ownerMapper.get(entityId).getToken().equals(playerToken)) {
        continue;
      }
      var playerMaterial = playerMaterialMapper.get(entityId);
      if (materials.containsKey(playerMaterial.getMaterial())) {
        var material = materials.get(playerMaterial.getMaterial());
        if (material.getAmount() > playerMaterial.getValue())
          return false;
      }
    }
    return true;
  }

  public void removeMaterials(String playerToken, Map<MaterialBase, MaterialUnit> materials) {
    for (int i = 0; i < ownerPlayerMaterialSubscriber.getEntities().size(); i++) {
      int entityId = ownerPlayerMaterialSubscriber.getEntities().get(i);
      if (ownerMapper.get(entityId).getToken().equals(playerToken)) {
        var playerMaterial = playerMaterialMapper.get(entityId);
        if (materials.containsKey(playerMaterial.getMaterial())) {
          var material = materials.get(playerMaterial.getMaterial());
          playerMaterial.setValue(playerMaterial.getValue() - material.getAmount());
          setDirty(entityId, PlayerMaterial.class, world);
        }
      }
    }
  }

  public void giveMaterialsToPlayers() {
    var incomes = calculateIncomes();

    for (int i = 0; i < ownerPlayerMaterialSubscriber.getEntities().size(); i++) {
      int entityId = ownerPlayerMaterialSubscriber.getEntities().get(i);
      var playerToken = ownerMapper.get(entityId).getToken();
      var playerMaterial = playerMaterialMapper.get(entityId);

      var playerIncome = incomes.get(playerToken);
      if (playerIncome == null) {
        continue;
      }
      var incomeValue = playerIncome.get(playerMaterial.getMaterial());
      playerMaterial.setValue(playerMaterial.getValue() + incomeValue);
      setDirty(entityId, PlayerMaterial.class, world);
    }
  }

  public int getPlayerMaterial(String playerToken, MaterialBase materialBase) {
    for (int i = 0; i < ownerPlayerMaterialSubscriber.getEntities().size(); i++) {
      int entityId = ownerPlayerMaterialSubscriber.getEntities().get(i);
      if (ownerMapper.get(entityId).getToken().equals(playerToken)) {
        if (playerMaterialMapper.get(entityId).getMaterial() == materialBase)
          return playerMaterialMapper.get(entityId).getValue();
      }
    }
    return 0;
  }

  public Map<String, Map<MaterialBase, Integer>> calculateIncomes() {
    var incomes = new HashMap<String, Map<MaterialBase, Integer>>();

    for (int i = 0; i < ownerFieldsSubscriber.getEntities().size(); i++) {
      var entityId = ownerFieldsSubscriber.getEntities().get(i);

      var ownerToken = ownerMapper.get(entityId).getToken();
      if (!incomes.containsKey(ownerToken)) {
        incomes.put(ownerToken, new EnumMap<>(MaterialBase.class));
        for (var material : MaterialBase.values()) {
          incomes.get(ownerToken).put(material, 0);

        }
      }

      var field = fieldMapper.get(entityId);
      var subFields = field.getSubFields();
      for (var subFieldEntityId : subFields.toArray()) {
        var materialIncome = materialIncomeMapper.get(subFieldEntityId);
        var subfield = subfieldMapper.get(subFieldEntityId);
        for (MaterialUnit materialUnit : materialIncome.getMaterialIncomes()) {
          var previousValue = incomes.get(ownerToken).get(materialUnit.getBase());
          int addAmount = materialUnit.getAmount();
          if (subfield.hasBuilding() && !underConstructionMapper.has(subfield.getBuilding())) {
            addAmount = calculateWithBuildingImpact(addAmount, subfield.getBuilding(), materialUnit.getBase());
          }
          incomes.get(ownerToken).put(materialUnit.getBase(), previousValue + addAmount);
        }
      }
    }

    for (Map.Entry<String, Map<MaterialBase, Integer>> entry : incomes.entrySet()) {
      for (Map.Entry<MaterialBase, Integer> playerEntry : entry.getValue().entrySet()) {
        var newValue = applyTechnologiesForMaterialImpact(playerEntry.getKey(), playerEntry.getValue(), entry.getKey());
        playerEntry.setValue(newValue);
      }
    }
    return incomes;
  }

  private int calculateWithBuildingImpact(int addAmount, int buildingEntityId, MaterialBase materialBase) {
    var entityConfigId = entityConfigIdMapper.get(buildingEntityId);
    var buildingConfig = gameConfigAssets.getGameConfigs().get(BuildingConfig.class, entityConfigId.getId());
    if (buildingConfig.getImpact().getBuildingType() == BuildingType.MATERIALS_BUILDING) {
      for (BuildingImpactValue buildingImpactValue : buildingConfig.getImpact().getBuildingImpactValues()) {
        if (buildingImpactValue.getParameter().name.equals(materialBase.name)) {
          var operation = buildingImpactValue.getOperation();
          int newAmount = operation.function.apply((float) addAmount, (float) buildingImpactValue.getValue()).intValue();
          return newAmount;
        }
      }
    }
    return addAmount;
  }

  public Integer applyTechnologiesForMaterialImpact(MaterialBase materialBase, Integer oldValue, String playerToken) {
    for (int i = 0; i < researchedOwnerSubscriber.getEntities().size(); i++) {
      int entityId = researchedOwnerSubscriber.getEntities().get(i);
      String owner = ownerMapper.get(entityId).getToken();
      var impact = technologyMapper.get(entityId).getImpact();
      if (owner.equals(playerToken) && impact.getTechnologyImpactType() == TechnologyImpactType.MATERIAL_IMPACT) {
        for (TechnologyImpactValue technologyImpactValue : impact.getTechnologyImpactValues()) {
          var parameter = technologyImpactValue.getParameter();
          if (MaterialBase.valueOf(parameter.name()) == materialBase) {
            var operation = technologyImpactValue.getOperation();
            var value = technologyImpactValue.getValue();
            return operation.function.apply((float) oldValue, (float) value).intValue();
          }
        }
      }
    }
    return oldValue;
  }
}
