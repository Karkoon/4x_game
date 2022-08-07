package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.server.initialize.SubfieldMapInitializer;
import com.mygdx.game.server.network.GameRoomSyncer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class FieldFactory implements EntityFactory<FieldConfig> {

  private final ComponentMapper<Field> fieldMapper;
  private final GameRoomSyncer syncer;
  private final SubfieldMapInitializer subMapInitializer;
  private final ComponentFactory componentFactory;

  @Inject
  public FieldFactory(
      @NonNull World world,
      @NonNull GameRoomSyncer gameRoomSyncer,
      @NonNull SubfieldMapInitializer subMapInitializer,
      @NonNull ComponentFactory componentFactory
  ) {
    this.fieldMapper = world.getMapper(Field.class);
    this.syncer = gameRoomSyncer;
    this.subMapInitializer = subMapInitializer;
    this.componentFactory = componentFactory;
  }

  @Override
  public void createEntity(int entityId, @NonNull FieldConfig config) {
    componentFactory.setUpEntityConfig(config, entityId);
    setUpField(entityId);
  }

  private void setUpField(int entityId) {
    var field = fieldMapper.create(entityId);
    var subFields = subMapInitializer.initializeSubarea(entityId);
    field.setSubFields(subFields);
    syncer.sendComponent(field, entityId);
  }
}
