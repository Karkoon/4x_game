package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.MaterialComponent;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.SubField;
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

@GameInstanceScope
@Log
public class ComponentFactory {

  private final GameRoom room;
  private final World world;
  private final ComponentClassToIndexCache componentIndicesCache;

  private ComponentMapper<SubField> subFieldMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<Unit> unitMapper;
  private ComponentMapper<Building> buildingMapper;
  private ComponentMapper<CanAttack> canAttackMapper;
  private ComponentMapper<ChangeSubscribers> changeSubscribersMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<DirtyComponents> dirtyMapper;
  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<FriendlyOrFoe> friendlyOrFoeMapper;
  private ComponentMapper<MaterialComponent> materialMapper;
  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<SharedComponents> sharedComponentsMapper;
  private ComponentMapper<SightlineSubscribers> sightlineSubscribersMapper;
  private ComponentMapper<Stats> statsMapper;

  @Inject
  public ComponentFactory(
      GameRoom room,
      World world,
      ComponentClassToIndexCache componentIndicesCache
  ) {
    this.room = room;
    this.world = world;
    this.world.inject(this);
    this.componentIndicesCache = componentIndicesCache;
    createAndDeleteEntityWithAllComponents();
  }

  private void createAndDeleteEntityWithAllComponents() {
    var entity = createEntityId();
    buildingMapper.create(entity);
    changeSubscribersMapper.create(entity);
    canAttackMapper.create(entity);
    coordinatesMapper.create(entity);
    dirtyMapper.create(entity);
    entityConfigIdMapper.create(entity);
    fieldMapper.create(entity);
    friendlyOrFoeMapper.create(entity);
    materialMapper.create(entity);
    nameMapper.create(entity);
    sharedComponentsMapper.create(entity);
    sightlineSubscribersMapper.create(entity);
    subFieldMapper.create(entity);
    world.delete(entity); //todo create a system to do it automatically?
  }

  public int createEntityId() {
    return world.create();
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
    var friendlies = new Bits(room.getNumberOfClients());
    if (nullableClient != null) {
      friendlies.set(room.getClients().indexOf(nullableClient));
    }
    friendlyOrFoe.setFriendlies(friendlies);
  }

  public void createNameComponent(int entityId, String name) {
    var nameComp = nameMapper.create(entityId);
    nameComp.setName(name);
  }

  public void createOwnerComponent(int entityId, Client owner) {
    var ownerComp = ownerMapper.create(entityId);
    ownerComp.setToken(owner.getPlayerToken());
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
    var subscribedClients = new Bits(room.getNumberOfClients());
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
  }

  public void createSubFieldComponent(int fieldId, int entityId) {
    var subField = subFieldMapper.create(entityId);
    subField.setParent(fieldId);
  }

  public void createMaterialComponent(int entityId, MaterialBase materialBase) {
    var materialComp = materialMapper.create(entityId);
    materialComp.setMaterial(materialBase);
    materialComp.setValue(0);
  }

  public void createUnitComponent(int entityId) {
    unitMapper.create(entityId);
  }
}
