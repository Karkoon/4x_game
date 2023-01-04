package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.AppliedTechnologies;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.InRecruitment;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.core.ecs.component.MaterialIncome;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.Technology;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.ecs.component.Unit;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.ComponentClassToIndexCache;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.ecs.component.DirtyComponents;
import com.mygdx.game.server.ecs.component.FriendlyOrFoe;
import com.mygdx.game.server.ecs.component.SharedComponents;
import com.mygdx.game.server.ecs.component.SightlineSubscribers;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Random;

@GameInstanceScope
@Log
public class ComponentFactory {

  private final ComponentClassToIndexCache componentIndicesCache;
  private final GameRoom gameRoom;
  private final Random random;
  private final World world;

  private ComponentMapper<AppliedTechnologies> appliedTechnologiesMapper;
  private ComponentMapper<Building> buildingMapper;
  private ComponentMapper<CanAttack> canAttackMapper;
  private ComponentMapper<ChangeSubscribers> changeSubscribersMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<DirtyComponents> dirtyMapper;
  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<FriendlyOrFoe> friendlyOrFoeMapper;
  private ComponentMapper<InRecruitment> inRecruitmentMapper;
  private ComponentMapper<InResearch> inResearchMapper;
  private ComponentMapper<MaterialIncome> materialIncomeMapper;
  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<PlayerMaterial> playerMaterialMapper;
  private ComponentMapper<SharedComponents> sharedComponentsMapper;
  private ComponentMapper<SightlineSubscribers> sightlineSubscribersMapper;
  private ComponentMapper<Stats> statsMapper;
  private ComponentMapper<SubField> subFieldMapper;
  private ComponentMapper<Technology> technologyMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;
  private ComponentMapper<Unit> unitMapper;

  @Inject
  public ComponentFactory(
      ComponentClassToIndexCache componentIndicesCache,
      GameRoom gameRoom,
      World world
  ) {
    this.componentIndicesCache = componentIndicesCache;
    this.gameRoom = gameRoom;
    this.random = new Random();
    this.world = world;
    this.world.inject(this);
    createAndDeleteEntityWithAllComponents();
  }

  private void createAndDeleteEntityWithAllComponents() {
    var entity = createEntityId();
    appliedTechnologiesMapper.create(entity);
    buildingMapper.create(entity);
    changeSubscribersMapper.create(entity);
    canAttackMapper.create(entity);
    coordinatesMapper.create(entity);
    dirtyMapper.create(entity);
    entityConfigIdMapper.create(entity);
    fieldMapper.create(entity);
    friendlyOrFoeMapper.create(entity);
    inRecruitmentMapper.create(entity);
    inResearchMapper.create(entity);
    nameMapper.create(entity);
    sharedComponentsMapper.create(entity);
    sightlineSubscribersMapper.create(entity);
    subFieldMapper.create(entity);
    technologyMapper.create(entity);
    underConstructionMapper.create(entity);
    world.delete(entity); //todo create a system to do it automatically?
  }

  public int createEntityId() {
    return world.create();
  }

  public void createAppliedTechnologies(int entityId) {
    appliedTechnologiesMapper.create(entityId);
  }

  public void createBuildingComponent(int entityId, int parent) {
    var building = buildingMapper.create(entityId);
    building.setParent(parent);
    var subField = subFieldMapper.get(parent);
    subField.setBuilding(entityId);
  }

  public void createCanAttackComponent(int entityId) {
    canAttackMapper.create(entityId).setCanAttack(true);
  }

  public void createChangeSubscribersComponent(int entityId) {
    changeSubscribersMapper.create(entityId);
  }

  public void createChangeSubscribersComponentFast(int entityId, int clientIndex) {
    var changeSubscribers = changeSubscribersMapper.create(entityId);
    changeSubscribers.getChangedSubscriptionState().set(clientIndex);
    changeSubscribers.getClients().flip(clientIndex);
  }

  public void createCoordinateComponent(Coordinates coordinates, int entityId) {
    var position = coordinatesMapper.create(entityId);
    position.setCoordinates(coordinates);
  }

  /**
   * Use when creating a component that needs to be sent at the beginning to the owner and doesn't change it's state.
   * For instance the technology entity components don't change at this moment.
   * It's a workaround to the problem of currently not having a way to
   * @param entityId
   * @param dirtyComps
   */
  public void createDirtyComponent(int entityId, Class... dirtyComps) {
    for (int i = 0; i < dirtyComps.length; i++) {
      var toDirty = dirtyComps[i];
      setDirty(entityId, toDirty);
    }
  }

  private void setDirty(int entityId, Class component) {
    var componentIndex = world.getComponentManager().getTypeFactory().getIndexFor(component);
    dirtyMapper.create(entityId).getDirtyComponents().set(componentIndex);
  }

  public void setUpEntityConfig(@NonNull Config config, int entityId) {
    var configId = config.getId();
    var entityConfigIdComponent = entityConfigIdMapper.create(entityId);
    entityConfigIdComponent.setId(configId);
  }

  public void createFieldComponent(int entityId, IntArray subfields) {
    var field = fieldMapper.create(entityId);
    field.setSubFields(subfields);
  }

  public void createFriendlyOrFoeComponent(
      int entityId,
      Client nullableClient
  ) {
    var friendlyOrFoe = friendlyOrFoeMapper.create(entityId);
    var friendlies = new Bits(gameRoom.getNumberOfClients());
    if (nullableClient != null) {
      friendlies.set(gameRoom.getClients().indexOf(nullableClient));
    }
    friendlyOrFoe.setFriendlies(friendlies);
  }

  public void createInRecruitmentComponent(
      int entityId,
      int turnLeft,
      long unitConfigId,
      String clientToken
  ) {

    var inRecruitment = inRecruitmentMapper.create(entityId);
    inRecruitment.setTurnLeft(turnLeft);
    inRecruitment.setUnitConfigId(unitConfigId);
    inRecruitment.setClientToken(clientToken);
  }

  public void createInResearchComponent(
      int entityId,
      int requiredScience
  ) {

    var inResearch = inResearchMapper.create(entityId);
    inResearch.setConfigRequiredScience(requiredScience);
    inResearch.setScienceLeft(requiredScience);
  }

  public void createMaterialIncomeComponent(SubFieldConfig config, int entityId) {
    var materialComp = materialIncomeMapper.create(entityId);
    var materialUnits = config.getMaterialProductions().get(
      random.nextInt(config.getMaterialProductions().size())
    );
    materialComp.setMaterialIncomes(materialUnits);
  }

  public void createNameComponent(int entityId, String name) {
    var nameComp = nameMapper.create(entityId);
    nameComp.setName(name);
  }

  public void createOwnerComponent(int entityId, Client owner) {
    var ownerComp = ownerMapper.create(entityId);
    ownerComp.setToken(owner.getPlayerToken());
  }

  public void createPlayerMaterialComponent(int entityId, MaterialBase materialBase) {
    var materialComp = playerMaterialMapper.create(entityId);
    materialComp.setMaterial(materialBase);
    materialComp.setValue(0);
  }

  public void createSharedComponents(
      int entityId,
      Class<? extends Component>[] compsToSyncWithFriendly,
      Class<? extends Component>[] compsToSyncWithEnemy
  ) {
    var sharedComponents = sharedComponentsMapper.create(entityId);
    sharedComponents.setFriendlies(componentIndicesCache.getIndicesFor(compsToSyncWithFriendly));
    sharedComponents.setFoes(componentIndicesCache.getIndicesFor(compsToSyncWithEnemy));
  }

  public void createSightlineSubscribersComponent(
      int entityId,
      int sightlineRadius
  ) {
    var sightlineSubscribers = sightlineSubscribersMapper.create(entityId);
    sightlineSubscribers.setSightlineRadius(sightlineRadius);
    var subscribedClients = new Bits(gameRoom.getNumberOfClients());
    sightlineSubscribers.setClients(subscribedClients);
  }

  public void createStatsComponent(int entityId, UnitConfig config) {
    var stats = statsMapper.create(entityId);
    stats.setHp(config.getMaxHp());
    stats.setMaxHp(config.getMaxHp());
    stats.setDefense(config.getDefense());
    stats.setSightRadius(config.getSightRadius());
    stats.setAttackPower(config.getAttackPower());
    stats.setMoveRange(config.getMoveRange());
    stats.setMaxMoveRange(config.getMoveRange());
    stats.setAttackRange(config.getAttackRange());
  }

  public void createSubFieldComponent(int fieldId, int entityId) {
    var subField = subFieldMapper.create(entityId);
    subField.setParent(fieldId);
  }

  public void createTechnologyComponent(int entityId, @NonNull TechnologyConfig config) {
    var technology = technologyMapper.create(entityId);
    technology.setImpact(config.getImpact());
  }

  public void createUnitComponent(int entityId) {
    unitMapper.create(entityId);
  }

  public void createUnderConstruction(BuildingConfig config, int entityId, int parent) {
    var underConstruction = underConstructionMapper.create(entityId);
    underConstruction.setBuildingConfigId((int) config.getId());
    underConstruction.setTurnLeft(config.getTurnAmount());
    underConstruction.setParentSubfield(parent);

    var subField = subFieldMapper.get(parent);
    subField.setBuilding(entityId);
  }
}
