package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.server.initialize.SubMapInitializer;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.GameRoomSyncer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class FieldFactory extends EntityFactory<FieldConfig> {

  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private final ComponentMapper<Field> fieldMapper;
  private final GameRoomSyncer syncer;
  private final SubMapInitializer subMapInitializer;

  @Inject
  public FieldFactory(
      @NonNull World world,
      @NonNull GameScreenAssets assets,
      @NonNull GameRoomSyncer gameRoomSyncer,
      @NonNull SubMapInitializer subMapInitializer
  ) {
    super(world, assets);
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.entityConfigIdMapper = world.getMapper(EntityConfigId.class);
    this.fieldMapper = world.getMapper(Field.class);
    this.syncer = gameRoomSyncer;
    this.subMapInitializer = subMapInitializer;
  }

  @Override
  public int createEntity(@NonNull FieldConfig config, @NonNull Coordinates coordinates, Client clientOwner) {
    var entity = world.create();

    var position = setUpCoordinates(coordinates, entity);
    var entityConfigId = setUpEntityConfig(config, entity);
    var fieldComponent = setUpField(entity, clientOwner);

    syncer.sendComponent(position, entity);
    syncer.sendComponent(entityConfigId, entity);
    syncer.sendComponent(fieldComponent, entity);
    return entity;
  }

  private EntityConfigId setUpEntityConfig(@NonNull FieldConfig config, int entityId) {
    var entityConfigId = config.getId();
    var entityConfigIdComponent = entityConfigIdMapper.create(entityId);
    entityConfigIdComponent.setId(entityConfigId);
    return entityConfigIdComponent;
  }

  private Coordinates setUpCoordinates(Coordinates coordinates, int entityId) {
    var result = coordinatesMapper.create(entityId);
    result.setCoordinates(coordinates);
    return result;
  }

  private Field setUpField(int entityId, Client clientOwner) {
    var field = fieldMapper.create(entityId);
    IntArray subFields = subMapInitializer.initializeSubarea(entityId, clientOwner);
    field.setSubFields(subFields);
    return field;
  }
}
