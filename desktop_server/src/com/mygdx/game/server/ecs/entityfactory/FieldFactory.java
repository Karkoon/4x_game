package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.InRecruitment;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.initialize.SubfieldMapInitializer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class FieldFactory {

  private final ComponentFactory componentFactory;
  private final SubfieldMapInitializer subMapInitializer;

  @Inject
  public FieldFactory(
      ComponentFactory componentFactory,
      SubfieldMapInitializer subMapInitializer
  ) {
    this.componentFactory = componentFactory;
    this.subMapInitializer = subMapInitializer;
  }

  public int createEntity(@NonNull FieldConfig config, Coordinates coordinate) {
    int entityId = componentFactory.createEntityId();
    componentFactory.createCoordinateComponent(coordinate, entityId);
    componentFactory.createNameComponent(entityId, "field " + config.getName() + " " + entityId);
    componentFactory.setUpEntityConfig(config, entityId);
    var subfields = subMapInitializer.initializeSubarea(entityId, config);
    componentFactory.createFieldComponent(entityId, subfields);
    componentFactory.createChangeSubscribersComponent(entityId);
    componentFactory.createFriendlyOrFoeComponent(entityId, null);
    componentFactory.createSightlineSubscribersComponent(entityId, 0);
    var componentsToSend = new Class[]{Coordinates.class, EntityConfigId.class, Field.class, Owner.class, InRecruitment.class};
    componentFactory.createDirtyComponent(entityId, componentsToSend);
    componentFactory.createSharedComponents(entityId,
        componentsToSend,
        new Class[]{Coordinates.class, EntityConfigId.class, Field.class, Owner.class, InRecruitment.class}
    );
    return entityId;
  }
}
