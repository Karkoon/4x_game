package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.MoveRange;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.MaterialComponent;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.SubField;
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

import javax.inject.Inject;

@GameInstanceScope
public class ComponentFactory {

  private final GameRoom room;
  private final World world;
  private final ComponentClassToIndexCache componentIndicesCache;

  private ComponentMapper<SubField> subFieldMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<MoveRange> moveRangeMapper;
  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<SightlineSubscribers> sightlineSubscribersMapper;
  private ComponentMapper<SharedComponents> sharedComponentsMapper;
  private ComponentMapper<FriendlyOrFoe> friendlyOrFoeMapper;
  private ComponentMapper<ChangeSubscribers> changeSubscribersMapper;
  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<Stats> statsMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<MaterialComponent> materialMapper;
  private ComponentMapper<DirtyComponents> dirtyMapper;
  private ComponentMapper<CanAttack> canAttackMapper;

  @Inject
  public ComponentFactory(
      @NonNull GameRoom room,
      @NonNull World world,
      @NonNull ComponentClassToIndexCache componentIndicesCache
  ) {
    this.room = room;
    this.world = world;
    this.world.inject(this);
    this.componentIndicesCache = componentIndicesCache;
    createAndDeleteEntityWithAllComponents();
  }

  private void createAndDeleteEntityWithAllComponents() {
    var entity = createEntityId();
    subFieldMapper.create(entity);
    fieldMapper.create(entity);
    coordinatesMapper.create(entity);
    moveRangeMapper.create(entity);
    entityConfigIdMapper.create(entity);
    friendlyOrFoeMapper.create(entity);
    changeSubscribersMapper.create(entity);
    nameMapper.create(entity);
    sightlineSubscribersMapper.create(entity);
    sharedComponentsMapper.create(entity);
    canAttackMapper.create(entity);
    materialMapper.create(entity);
    dirtyMapper.create(entity);
    world.delete(entity); //todo create a system to do it automatically?
  }

  public int createEntityId() {
    return world.create();
  }

  public void createNameComponent(int entityId, String name) {
    var nameComp = nameMapper.create(entityId);
    nameComp.setName(name);
  }

  public void createCoordinateComponent(Coordinates coordinates, int entityId) {
    var position = coordinatesMapper.create(entityId);
    position.setCoordinates(coordinates);
  }

  public void createMoveRangeComponent(int moveRange, int entityId){
    var range = moveRangeMapper.create(entityId);
    range.setMoveRange(moveRange);
    //TODO: Może current inicjalizowany od 0?
    range.setCurrentRange(moveRange);
  }

  public void createSubFieldComponent(int fieldId, int entityId) {
    var subField = subFieldMapper.create(entityId);
    subField.setParent(fieldId);
  }

  public void createFieldComponent(int entityId, IntArray subfields) {
    var field = fieldMapper.create(entityId);
    field.setSubFields(subfields);
  }

  public void setUpEntityConfig(@NonNull Config config, int entityId) {
    var configId = config.getId();
    var entityConfigIdComponent = entityConfigIdMapper.create(entityId);
    entityConfigIdComponent.setId(configId);
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

  public void createSightlineSubscribersComponent(
      int entityId,
      int sightlineRadius
  ) {
    var sightlineSubscribers = sightlineSubscribersMapper.create(entityId);
    sightlineSubscribers.setSightlineRadius(sightlineRadius);
    var subscribedClients = new Bits(room.getNumberOfClients());
    sightlineSubscribers.setClients(subscribedClients);
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

  public void createChangeSubscribersComponent(int entityId) {
    changeSubscribersMapper.create(entityId);
  }

  public void createStatsComponent(int entityId, UnitConfig config) {
    var stats = statsMapper.create(entityId);
    stats.setHp(config.getMaxHp());
    stats.setMaxHp(config.getMaxHp());
    stats.setSpeed(config.getSpeed());
    stats.setDefense(config.getDefense());
    stats.setSightRadius(config.getSightRadius());
    stats.setAttackPower(config.getAttackPower());
    stats.setMoveRange(config.getMoveRange());
  }

  public void createOwnerComponent(int entityId, Client owner) {
    var ownerComp = ownerMapper.create(entityId);
    ownerComp.setToken(owner.getPlayerToken());
  }

  public void createMaterialComponent(int entityId, MaterialBase materialBase) {
    var materialComp = materialMapper.create(entityId);
    materialComp.setMaterial(materialBase);
    materialComp.setValue(0);
  }

  public void createCanAttackComponent(int entityId) {
    canAttackMapper.create(entityId).setCanAttack(true);
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

}
