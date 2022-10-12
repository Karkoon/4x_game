package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.server.di.GameInstanceScope;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class SubFieldFactory {

  private final ComponentFactory componentFactory;

  @Inject
  public SubFieldFactory(
      ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  public int createEntity(@NonNull SubFieldConfig config, Coordinates coordinates, int parentField) {
    int entityId = componentFactory.createEntityId();
    componentFactory.createCoordinateComponent(coordinates, entityId);
    componentFactory.setUpEntityConfig(config, entityId);
    componentFactory.createNameComponent(entityId, "subfield " + config.getName() + " " + entityId + " " + parentField);
    componentFactory.createSubFieldComponent(parentField, entityId);
    componentFactory.createMaterialIncomeComponent(config, entityId);
    componentFactory.createChangeSubscribersComponent(entityId);
    componentFactory.createFriendlyOrFoeComponent(entityId, null);
    componentFactory.createSharedComponents(entityId,
        new Class[]{Coordinates.class, EntityConfigId.class, SubField.class},
        new Class[]{Coordinates.class, EntityConfigId.class, SubField.class});
    return entityId;
  }
}
