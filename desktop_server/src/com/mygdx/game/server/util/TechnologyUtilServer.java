package com.mygdx.game.server.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.core.ecs.component.AppliedTechnologies;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Researched;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.Technology;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.ecs.component.Unit;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import com.mygdx.game.core.model.TechnologyImpact;
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

  private final MaterialUtilServer materialUtilServer;
  private final World world;

  private ComponentMapper<AppliedTechnologies> appliedTechnologiesMapper;
  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<InResearch> inResearchMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Researched> researchedMapper;
  private ComponentMapper<Stats> statsMapper;
  private ComponentMapper<SubField> subfieldMapper;
  private ComponentMapper<Technology> technologyMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;

  @AspectDescriptor(all = {InResearch.class})
  private EntitySubscription inResearchSubscriber;

  @AspectDescriptor(all = {Unit.class, Owner.class})
  private EntitySubscription unitOwnerSubscriber;

  @AspectDescriptor(all = {UnderConstruction.class})
  private EntitySubscription underConstructionSubscriber;

  @AspectDescriptor(all = {Researched.class, Owner.class})
  private EntitySubscription researchedOwnerSubscriber;

  @Inject
  TechnologyUtilServer(
      MaterialUtilServer materialUtilServer,
      World world
  ) {
    world.inject(this);
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
    var technologyImpact = technologyMapper.get(technologyEntityId).getImpact();
    switch (technologyImpact.getTechnologyImpactType()) {
      case UNIT_IMPACT -> {
        for (int i = 0; i < unitOwnerSubscriber.getEntities().size(); i++) {
          int entityId = unitOwnerSubscriber.getEntities().get(i);
          if (ownerMapper.get(entityId).getToken().equals(owner.getToken()))
            applyTechnologyToUnit(technologyImpact, entityId, (int) entityConfigId.getId());
        }
      }
      case BUILDING_IMPACT -> {
        for (int i = 0; i < underConstructionSubscriber.getEntities().size(); i++) {
          int entityId = underConstructionSubscriber.getEntities().get(i);
          int parent = subfieldMapper.get(underConstructionMapper.get(entityId).getParentSubfield()).getParent();
          if (ownerMapper.get(parent).getToken().equals(owner.getToken()))
            applyTechnologyToBuilding(technologyImpact, entityId);
        }
      }
    }
  }

  public void applyTechnologyToNewEntities(int newEntityId, String ownerToken, TechnologyImpactType technologyType) {
    var entities = researchedOwnerSubscriber.getEntities();
    switch (technologyType) {
      case UNIT_IMPACT -> {
        for (int i = 0; i < entities.size(); i++) {
          int entityId = entities.get(i);
          var impact = technologyMapper.get(entityId).getImpact();
          if (impact.getTechnologyImpactType().equals(technologyType)
                  && ownerMapper.get(entityId).getToken().equals(ownerToken)) {
            log.info("Apply technology " + entityConfigIdMapper.get(entityId).getId() + " to unit " + newEntityId);
            applyTechnologyToUnit(impact, newEntityId, (int) entityConfigIdMapper.get(entityId).getId());
          }
        }
      }
      case BUILDING_IMPACT -> {
        for (int i = 0; i < entities.size(); i++) {
          int entityId = entities.get(i);
          var impact = technologyMapper.get(entityId).getImpact();
          int parent = subfieldMapper.get(underConstructionMapper.get(newEntityId).getParentSubfield()).getParent();
          if (impact.getTechnologyImpactType().equals(technologyType)
                  && ownerMapper.get(parent).getToken().equals(ownerToken)) {
            log.info("Apply technology " + entityConfigIdMapper.get(entityId).getId() + " to building " + newEntityId);
            applyTechnologyToBuilding(impact, newEntityId);
          }
        }
      }
    }

  }

  private void applyTechnologyToUnit(TechnologyImpact technologyImpact, int entityId, int techConfigId) {
    var appliedTechnologies = appliedTechnologiesMapper.get(entityId);
    if (!appliedTechnologies.getTechnologies().contains(techConfigId)) {
      var stats = statsMapper.get(entityId);
      var technologyImpactValues = technologyImpact.getTechnologyImpactValues();
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
      appliedTechnologies.add(techConfigId);
    }
  }

  private void applyTechnologyToBuilding(TechnologyImpact technologyImpact, int entityId) {
    var technologyImpactValues = technologyImpact.getTechnologyImpactValues();
    var underConstruction = underConstructionMapper.get(entityId);
    for (TechnologyImpactValue technologyImpactValue : technologyImpactValues) {
      var operation = technologyImpactValue.getOperation();
      var parameter = technologyImpactValue.getParameter();
      var value = technologyImpactValue.getValue();
      switch (parameter) {
        case TIME ->
          underConstruction.setTurnLeft(Math.max(1, operation.function.apply((float) underConstruction.getTurnLeft(), (float) value).intValue()));
      }
    }
  }

  public Map<MaterialBase, MaterialUnit> getReducedParametersForBuilding(Map<MaterialBase, MaterialUnit> materials, String playerToken) {
    var newMaterials = new HashMap<MaterialBase, MaterialUnit>();
    for (MaterialBase materialBase : materials.keySet()) {
      newMaterials.put(materialBase, materials.get(materialBase));
    }

    for (int i = 0; i < researchedOwnerSubscriber.getEntities().size(); i++) {
      int techEntityId = researchedOwnerSubscriber.getEntities().get(i);
      var technologyImpact = technologyMapper.get(techEntityId).getImpact();
      if (technologyImpact.getTechnologyImpactType().equals(TechnologyImpactType.BUILDING_IMPACT)
              && ownerMapper.get(techEntityId).getToken().equals(playerToken)) {
        log.info("Apply technology " + entityConfigIdMapper.get(techEntityId).getId() + " to building materials");
        for (TechnologyImpactValue technologyImpactValue : technologyImpact.getTechnologyImpactValues()) {
          var operation = technologyImpactValue.getOperation();
          var parameter = technologyImpactValue.getParameter();
          var value = technologyImpactValue.getValue();
          switch (parameter) {
            case GOLD -> {
              if (newMaterials.containsKey(MaterialBase.GOLD))
                newMaterials.get(MaterialBase.GOLD).setAmount(Math.max(operation.function.apply((float) newMaterials.get(MaterialBase.GOLD).getAmount(), (float) value).intValue(), 0));
            }
            case FOOD -> {
              if (newMaterials.containsKey(MaterialBase.FOOD))
                newMaterials.get(MaterialBase.FOOD).setAmount(Math.max(operation.function.apply((float) newMaterials.get(MaterialBase.FOOD).getAmount(), (float) value).intValue(), 0));
            }
            case PRODUCTION -> {
              if (newMaterials.containsKey(MaterialBase.PRODUCTION))
                newMaterials.get(MaterialBase.PRODUCTION).setAmount(Math.max(operation.function.apply((float) newMaterials.get(MaterialBase.PRODUCTION).getAmount(), (float) value).intValue(), 0));
            }
            case SCIENCE -> {
              if (newMaterials.containsKey(MaterialBase.SCIENCE))
                newMaterials.get(MaterialBase.SCIENCE).setAmount(Math.max(operation.function.apply((float) newMaterials.get(MaterialBase.SCIENCE).getAmount(), (float) value).intValue(), 0));
            }
          }
        }
      }
    }
    return newMaterials;
  }
}
