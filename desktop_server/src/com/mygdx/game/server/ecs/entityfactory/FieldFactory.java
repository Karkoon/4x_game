package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.initialize.SubfieldMapInitializer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class FieldFactory {

  private final SubfieldMapInitializer subMapInitializer;
  private final ComponentFactory componentFactory;

  @Inject
  public FieldFactory(
      @NonNull SubfieldMapInitializer subMapInitializer,
      @NonNull ComponentFactory componentFactory
  ) {
    this.subMapInitializer = subMapInitializer;
    this.componentFactory = componentFactory;
  }

  public void createEntity(@NonNull FieldConfig config, Coordinates coordinate) {
    int entityId = componentFactory.createEntityId();
    componentFactory.createCoordinateComponent(coordinate, entityId);
    componentFactory.createNameComponent(entityId, "field " + config.getName() + " " + entityId);
    componentFactory.setUpEntityConfig(config, entityId);
    var subfields = subMapInitializer.initializeSubarea(entityId, config);
    componentFactory.createFieldComponent(entityId, subfields);
    componentFactory.createChangeSubscribersComponent(entityId);
    componentFactory.createFriendlyOrFoeComponent(entityId, null);
    componentFactory.createSharedComponents(entityId,
        new Class[]{Coordinates.class, EntityConfigId.class, Field.class},
        new Class[]{Coordinates.class, EntityConfigId.class, Field.class}
    );
  }
}
