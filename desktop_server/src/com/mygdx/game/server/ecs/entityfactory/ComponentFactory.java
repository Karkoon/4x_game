package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.config.Config;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.server.network.GameRoomSyncer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class ComponentFactory {

  private final GameRoomSyncer syncer;
  private final World world;

  private final ComponentMapper<SubField> subFieldMapper;
  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final ComponentMapper<EntityConfigId> entityConfigIdMapper;

  @Inject
  public ComponentFactory(
          @NonNull World world,
          @NonNull GameRoomSyncer syncer
  ) {
    this.syncer = syncer;
    this.world = world;

    this.subFieldMapper = world.getMapper(SubField.class);
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.entityConfigIdMapper = world.getMapper(EntityConfigId.class);
  }

  public int createEntityId() {
    return world.create();
  }

  public void createCoordinateComponent(Coordinates coordinates, int entityId) {
    var position = coordinatesMapper.create(entityId);
    position.setCoordinates(coordinates);
    syncer.sendComponent(position, entityId);
  }

  public void createSubFieldComponent(int fieldId, int entityId) {
    var subField = subFieldMapper.create(entityId);
    subField.setParent(fieldId);
    syncer.sendComponent(subField, entityId);
  }

  public void setUpEntityConfig(@NonNull Config config, int entityId) {
    var configId = config.getId();
    var entityConfigIdComponent = entityConfigIdMapper.create(entityId);
    entityConfigIdComponent.setId(configId);
    syncer.sendComponent(entityConfigIdComponent, entityId);
  }
}
