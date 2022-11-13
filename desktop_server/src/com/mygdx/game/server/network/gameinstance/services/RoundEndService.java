package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.InRecruitment;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.model.TechnologyImpactType;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.BuildingFactory;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.gameinstance.StateSyncer;
import com.mygdx.game.server.util.MaterialUtilServer;
import com.mygdx.game.server.util.TechnologyUtilServer;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class RoundEndService extends WorldService {

  @AspectDescriptor(all = {Stats.class})
  private EntitySubscription unitSubscriber;
  @AspectDescriptor(all = {CanAttack.class})
  private EntitySubscription canAttackSubscriber;
  @AspectDescriptor(all = {InRecruitment.class})
  private EntitySubscription inRecruitmentSubscriber;
  @AspectDescriptor(all = {Owner.class, PlayerMaterial.class})
  private EntitySubscription ownerPlayerMaterialSubscriber;
  @AspectDescriptor(all = {UnderConstruction.class})
  private EntitySubscription underConstructionSubscriber;

  private ComponentMapper<CanAttack> canAttackMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<InRecruitment> inRecruitmentMapper;
  private ComponentMapper<Stats> statsMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;

  private final World world;

  private final BuildingFactory buildingFactory;
  private final GameConfigAssets gameConfigAssets;
  private final MaterialUtilServer materialUtilServer;
  private final GameRoom room;
  private final TechnologyUtilServer technologyUtilServer;
  private final UnitFactory unitFactory;
  private final StateSyncer stateSyncer;

  @Inject
  RoundEndService(
      World world,
      BuildingFactory buildingFactory,
      GameConfigAssets gameConfigAssets,
      MaterialUtilServer materialUtilServer,
      GameRoom room,
      TechnologyUtilServer technologyUtilServer,
      UnitFactory unitFactory,
      StateSyncer stateSyncer
  ) {
    world.inject(this);
    this.world = world;
    this.buildingFactory = buildingFactory;
    this.gameConfigAssets = gameConfigAssets;
    this.materialUtilServer = materialUtilServer;
    this.room = room;
    this.technologyUtilServer = technologyUtilServer;
    this.unitFactory = unitFactory;
    this.stateSyncer = stateSyncer;
  }

  public void makeEndRoundSteps() {
    log.info("End round, edit components");
    resetUnitComponents();
    researchTechnologies();
    giveMaterialsFromSubfields();
    constructBuildings();
    recruitUnits();
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

  private void researchTechnologies() {
    technologyUtilServer.researchTechnologies();
  }

  private void giveMaterialsFromSubfields() {
    materialUtilServer.giveMaterialsToPlayers();
  }

  private void constructBuildings() {
    var underConstructionEntities = underConstructionSubscriber.getEntities();
    for (int i = 0; i < underConstructionEntities.size(); i++) {
      int entityId = underConstructionEntities.get(i);
      var underConstruction = underConstructionMapper.get(entityId);
      underConstruction.setTurnLeft(underConstruction.getTurnLeft()-1);
      if (underConstruction.getTurnLeft() == 0) {
        int buildingConfigId = underConstruction.getBuildingConfigId();
        int parentSubfield = underConstruction.getParentSubfield();
        var buildingConfig = gameConfigAssets.getGameConfigs().get(BuildingConfig.class, buildingConfigId);
        this.buildingFactory.createEntity(entityId, buildingConfig, parentSubfield);
        this.underConstructionMapper.remove(entityId);
      }
    }
  }

  private void recruitUnits() {
    var inRecruitmentEntities = inRecruitmentSubscriber.getEntities();
    for (int i = 0; i < inRecruitmentEntities.size(); i++) {
      int entityId = inRecruitmentEntities.get(i);
      var inRecruitment = inRecruitmentMapper.get(entityId);
      inRecruitment.setTurnLeft(inRecruitment.getTurnLeft()-1);
      setDirty(entityId, InRecruitment.class, world);
      if (inRecruitment.getTurnLeft() == 0) {
        long unitConfigId = inRecruitment.getUnitConfigId();
        var coordinates = coordinatesMapper.get(entityId);
        var client = room.getClientByToken(inRecruitment.getClientToken());
        stateSyncer.sendComponentTo(inRecruitment, entityId, client);
        var unitConfig = gameConfigAssets.getGameConfigs().get(UnitConfig.class, unitConfigId);
        int unitEntityId = this.unitFactory.createEntity(unitConfig, coordinates, client);
        world.process();
        this.inRecruitmentMapper.remove(entityId);
        technologyUtilServer.applyTechnologyToNewEntities(unitEntityId, client.getPlayerToken(), TechnologyImpactType.UNIT_IMPACT);
      }
    }
  }
}
