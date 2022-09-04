package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.config.Config;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.ComponentClassToIndexCache;
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

  private final ComponentMapper<SubField> subFieldMapper;
  private final ComponentMapper<Field> fieldMapper;
  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private final ComponentMapper<SightlineSubscribers> sightlineSubscribersMapper;
  private final ComponentMapper<SharedComponents> sharedComponentsMapper;
  private final ComponentMapper<FriendlyOrFoe> friendlyOrFoeMapper;

  @Inject
  public ComponentFactory(
      @NonNull GameRoom room,
      @NonNull World world,
      @NonNull ComponentClassToIndexCache componentIndicesCache
  ) {
    this.room = room;
    this.world = world;
    this.componentIndicesCache = componentIndicesCache;
    this.subFieldMapper = world.getMapper(SubField.class);
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.entityConfigIdMapper = world.getMapper(EntityConfigId.class);
    this.fieldMapper = world.getMapper(Field.class);
    this.sightlineSubscribersMapper = world.getMapper(SightlineSubscribers.class);
    this.sharedComponentsMapper = world.getMapper(SharedComponents.class);
    this.friendlyOrFoeMapper = world.getMapper(FriendlyOrFoe.class);
  }

  public int createEntityId() {
    return world.create();
  }

  public void createCoordinateComponent(Coordinates coordinates, int entityId) {
    var position = coordinatesMapper.create(entityId);
    position.setCoordinates(coordinates);
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
    var friendlyOrFoe = friendlyOrFoeMapper.get(entityId);
    var friendlies = new Bits(room.getNumberOfClients());
    if (nullableClient != null) {
      friendlies.set(room.getClients().indexOf(nullableClient));
    }
    friendlyOrFoe.setFriendlies(friendlies);
  }

  public void createSightlineSubscribersComponent(
      int entityId,
      Client nullableClient
  ) {
    var sightlineSubscribers = sightlineSubscribersMapper.get(entityId);
    var subscribedClients = new Bits(room.getNumberOfClients());
    if (nullableClient != null) {
      subscribedClients.set(room.getClients().indexOf(nullableClient));
    }
    sightlineSubscribers.setClients(subscribedClients);
  }

  public void createSharedComponents(
      int entityId,
      Class<? extends Component>[] compsToSyncWithFriendly,
      Class<? extends Component>[] compsToSyncWithEnemy
  ) {
    var sharedComponents = sharedComponentsMapper.get(entityId);
    sharedComponents.setFriendlies(componentIndicesCache.getIndicesFor(compsToSyncWithFriendly));
    sharedComponents.setFoes(componentIndicesCache.getIndicesFor(compsToSyncWithEnemy));
  }

}
