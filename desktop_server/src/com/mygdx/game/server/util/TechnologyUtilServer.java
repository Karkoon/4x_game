package com.mygdx.game.server.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.utils.IntBag;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.AppliedTechnologies;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Researched;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.Unit;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import com.mygdx.game.core.model.TechnologyImpactType;
import com.mygdx.game.core.model.TechnologyImpactValue;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.network.gameinstance.services.WorldService;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@GameInstanceScope
@Log
public class TechnologyUtilServer extends WorldService {

  private final GameConfigAssets gameConfigAssets;
  private final MaterialUtilServer materialUtilServer;
  private final World world;

  private ComponentMapper<AppliedTechnologies> appliedTechnologiesMapper;
  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<InResearch> inResearchMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Researched> researchedMapper;
  private ComponentMapper<Stats> statsMapper;

  @AspectDescriptor(all = {InResearch.class})
  private EntitySubscription inResearchSubscriber;

  @AspectDescriptor(all = {Unit.class, Owner.class})
  private EntitySubscription unitOwnerSubscriber;

  @AspectDescriptor(all = {Researched.class, Owner.class})
  private EntitySubscription researchedOwnerSubscriber;

  @Inject
  TechnologyUtilServer(
      GameConfigAssets gameConfigAssets,
      MaterialUtilServer materialUtilServer,
      World world
  ) {
    world.inject(this);
    this.gameConfigAssets = gameConfigAssets;
    this.materialUtilServer = materialUtilServer;
    this.world = world;
  }

  public void researchTechnologies() {
    for (int i = 0; i < inResearchSubscriber.getEntities().size(); i++) {
      int technologyEntityId = inResearchSubscriber.getEntities().get(i);
      var inResearch = inResearchMapper.get(technologyEntityId);
      var owner = ownerMapper.get(technologyEntityId);
      int playerScience = materialUtilServer.getPlayerMaterial(owner.getToken(), MaterialBase.SCIENCE);
      int maxCanUse = Math.min(playerScience, inResearch.getScienceLeft());
      log.info("Player science to use " + playerScience);
      log.info("Science points left to research " + inResearch.getScienceLeft());
      if (maxCanUse > 0) {
        log.info("Try to remove science: " + maxCanUse);
        Map<MaterialBase, MaterialUnit> materialsToRemove = new HashMap<>();
        materialsToRemove.put(MaterialBase.SCIENCE, new MaterialUnit(MaterialBase.SCIENCE, maxCanUse));
        materialUtilServer.removeMaterials(owner.getToken(), materialsToRemove);
        inResearch.setScienceLeft(inResearch.getScienceLeft()-maxCanUse);
        setDirty(technologyEntityId, InResearch.class, world);
        if (inResearch.getScienceLeft() == 0) {
          log.info("Successfully researched");
          inResearchMapper.remove(technologyEntityId);
          researchedMapper.create(technologyEntityId);
          setDirty(technologyEntityId, Researched.class, world);
          applyTechnologyToExistingEntities(technologyEntityId, owner);
        }
      }
      world.process();
    }
  }

  private void applyTechnologyToExistingEntities(int technologyEntityId, Owner owner) {
    var entityConfigId = entityConfigIdMapper.get(technologyEntityId);
    var technologyConfig = gameConfigAssets.getGameConfigs().get(TechnologyConfig.class, entityConfigId.getId());
    switch (technologyConfig.getImpact().getTechnologyImpactType()) {
      case UNIT_IMPACT -> {
        for (int i = 0; i < unitOwnerSubscriber.getEntities().size(); i++) {
          int entityId = unitOwnerSubscriber.getEntities().get(i);
          if (ownerMapper.get(entityId).getToken().equals(owner.getToken()))
            applyTechnologyToUnit(technologyConfig, entityId);
        }
      }
      case MATERIAL_IMPACT -> {
        // TODO
      }
      case BUILDING_IMPACT -> {
        // TODO
      }
    }
  }

  public void applyTechnologyToNewEntities(int unitEntityId, TechnologyImpactType technologyType) {
    var owner = ownerMapper.get(unitEntityId);
    var entities = researchedOwnerSubscriber.getEntities();
    switch (technologyType) {
      case UNIT_IMPACT -> {
        for (int i = 0; i < entities.size(); i++) {
          int entityId = entities.get(i);
          if (gameConfigAssets.getGameConfigs().get(TechnologyConfig.class, entityConfigIdMapper.get(entityId).getId()).getImpact().getTechnologyImpactType().equals(technologyType) && ownerMapper.get(entityId).getToken().equals(owner.getToken())) {
            log.info("Apply technology " + entityConfigIdMapper.get(entityId).getId() + " to unit " + unitEntityId);
            applyTechnologyToUnit(gameConfigAssets.getGameConfigs().get(TechnologyConfig.class, entityConfigIdMapper.get(entityId).getId()), unitEntityId);
          }
        }
      }
      case BUILDING_IMPACT -> {
        // TODO
      }
      case MATERIAL_IMPACT -> {
        // TODO
      }
    }

  }

  private void applyTechnologyToUnit(TechnologyConfig technologyConfig, int entityId) {
    var appliedTechnologies = appliedTechnologiesMapper.get(entityId);
    if (!appliedTechnologies.getTechnologies().contains(technologyConfig.getId())) {
      var stats = statsMapper.get(entityId);
      var technologyImpactValues = technologyConfig.getImpact().getTechnologyImpactValues();
      for (TechnologyImpactValue technologyImpactValue : technologyImpactValues) {
        var operation = technologyImpactValue.getOperation();
        var parameter = technologyImpactValue.getParameter();
        var value = technologyImpactValue.getValue();
        switch (parameter) {
          case HP ->
            stats.setMaxHp(operation.function.apply((float) stats.getMaxHp(), (float) value).intValue());
          case ATTACK_POWER ->
            stats.setAttackPower(operation.function.apply((float) stats.getAttackPower(), (float) value).intValue());
          case DEFENSE ->
            stats.setDefense(operation.function.apply((float) stats.getDefense(), (float) value).intValue());
          case SIGHT_RADIUS ->
            stats.setSightRadius(operation.function.apply((float) stats.getSightRadius(), (float) value).intValue());
          case MOVE_RANGE ->
            stats.setMaxMoveRange(operation.function.apply((float) stats.getMaxMoveRange(), (float) value).intValue());
          case ATTACK_RANGE ->
            stats.setAttackRange(operation.function.apply((float) stats.getAttackRange(), (float) value).intValue());
        }
        setDirty(entityId, Stats.class, world);
      }
      appliedTechnologies.add(technologyConfig.getId());
    }
  }
}
