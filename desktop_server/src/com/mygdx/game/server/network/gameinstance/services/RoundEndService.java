package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.MaterialIncome;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import com.mygdx.game.server.di.GameInstanceScope;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Log
@GameInstanceScope
public class RoundEndService extends WorldService {

  @AspectDescriptor(all = {Stats.class})
  private EntitySubscription unitSubscriber;
  @AspectDescriptor(all = {CanAttack.class})
  private EntitySubscription canAttackSubscriber;
  @AspectDescriptor(all = {Owner.class, Field.class})
  private EntitySubscription ownerFieldsSubscriber;
  @AspectDescriptor(all = {Owner.class, PlayerMaterial.class})
  private EntitySubscription ownerPlayerMaterialSubscriber;

  private ComponentMapper<Stats> statsMapper;
  private ComponentMapper<CanAttack> canAttackMapper;
  private ComponentMapper<MaterialIncome> materialIncomeMapper;
  private ComponentMapper<PlayerMaterial> playerMaterialMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Field> fieldMapper;

  private final World world;

  @Inject
  RoundEndService(
      World world
  ) {
    world.inject(this);
    this.world = world;
  }

  public void makeEndRoundSteps() {
    log.info("End round, edit components");
    resetUnitComponents();
    giveMaterialsFromSubfields();
    world.process();
  }

  private void resetUnitComponents() {
    resetMovement();
    resetCanAttack();
  }

  private void resetMovement() {
    for (int i = 0; i < unitSubscriber.getEntities().size(); i++) {
      int entityId = unitSubscriber.getEntities().get(i);
      var stats = statsMapper.get(entityId);
      stats.setMoveRange(stats.getMaxMoveRange());
      setDirty(entityId, Stats.class, world);
    }
  }

  private void resetCanAttack() {
    for (int i = 0; i < canAttackSubscriber.getEntities().size(); i++) {
      int entityId = canAttackSubscriber.getEntities().get(i);
      var canAttack = canAttackMapper.get(entityId);
      canAttack.setCanAttack(true);
      setDirty(entityId, CanAttack.class, world);
    }
  }

  private void giveMaterialsFromSubfields() {
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

  private Map<String, Map<MaterialBase, Integer>> calculateIncomes() {
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
