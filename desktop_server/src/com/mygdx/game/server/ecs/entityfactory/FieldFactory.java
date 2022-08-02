package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.server.initialize.SubfieldMapInitializer;
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
  private final SubfieldMapInitializer subMapInitializer;

  @Inject
  public FieldFactory(
      @NonNull World world,
      @NonNull GameConfigAssets assets,
      @NonNull GameRoomSyncer gameRoomSyncer,
      @NonNull SubfieldMapInitializer subMapInitializer
  ) {
    super(world, assets);
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.entityConfigIdMapper = world.getMapper(EntityConfigId.class);
    this.fieldMapper = world.getMapper(Field.class);
    this.syncer = gameRoomSyncer;
    this.subMapInitializer = subMapInitializer;
  }

  @Override
  public void createEntity(int entityId, @NonNull FieldConfig config) {
    var entityConfigId = setUpEntityConfig(config, entityId);
    var fieldComponent = setUpField(entityId);

    syncer.sendComponent(entityConfigId, entityId);
    syncer.sendComponent(fieldComponent, entityId);
  }

  private EntityConfigId setUpEntityConfig(@NonNull FieldConfig config, int entityId) {
    var entityConfigId = config.getId();
    var entityConfigIdComponent = entityConfigIdMapper.create(entityId);
    entityConfigIdComponent.setId(entityConfigId);
    return entityConfigIdComponent;
  }

  private Field setUpField(int entityId) {
    var field = fieldMapper.create(entityId);
    var subFields = subMapInitializer.initializeSubarea(entityId);
    field.setSubFields(subFields);
    return field;
  }
}
