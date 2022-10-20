package com.mygdx.game.server.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.MaterialIncome;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.network.gameinstance.services.WorldService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@GameInstanceScope
public class MaterialUtilServer extends WorldService {

  @AspectDescriptor(all = {Owner.class, Field.class})
  private EntitySubscription ownerFieldsSubscriber;

  @AspectDescriptor(all = {Owner.class, PlayerMaterial.class})
  private EntitySubscription ownerPlayerMaterialSubscriber;

  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<MaterialIncome> materialIncomeMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<PlayerMaterial> playerMaterialMapper;

  private final World world;

  @Inject
  MaterialUtilServer(
      World world
  ) {
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

      var incomeValue = incomes.get(playerToken).get(playerMaterial.getMaterial());
      playerMaterial.setValue(playerMaterial.getValue() + incomeValue);
      setDirty(entityId, PlayerMaterial.class, world);
    }
  }

  public Map<String, Map<MaterialBase, Integer>> calculateIncomes() {
    var incomes = new HashMap<String, Map<MaterialBase, Integer>>();

    for (int i = 0; i < ownerFieldsSubscriber.getEntities().size(); i++) {
      int entityId = ownerFieldsSubscriber.getEntities().get(i);

      var ownerToken = ownerMapper.get(entityId).getToken();
      if (!incomes.containsKey(ownerToken)) {
        incomes.put(ownerToken, new HashMap<>());
        for (MaterialBase material : MaterialBase.values()) {
          incomes.get(ownerToken).put(material, 0);

        }
      }

      var field = fieldMapper.get(entityId);
      var subFields = field.getSubFields();
      for (int subFieldEntityId : subFields.toArray()) {
        var materialIncome = materialIncomeMapper.get(subFieldEntityId);
        for (MaterialUnit materialUnit : materialIncome.getMaterialIncomes()) {
          var previousValue = incomes.get(ownerToken).get(materialUnit.getBase());
          incomes.get(ownerToken).put(materialUnit.getBase(), previousValue + materialUnit.getAmount());
        }
      }
    }

    return incomes;
  }
}
