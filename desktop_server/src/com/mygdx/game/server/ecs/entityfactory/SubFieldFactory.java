package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class SubFieldFactory {

  private final ComponentFactory componentFactory;

  @Inject
  public SubFieldFactory(
      @NonNull ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  public int createEntity(@NonNull SubFieldConfig config, Coordinates coordinates, int parentField) {
    int entityId = componentFactory.createEntityId();
    componentFactory.createCoordinateComponent(coordinates, entityId);
    componentFactory.setUpEntityConfig(config, entityId);
    componentFactory.createSubFieldComponent(parentField, entityId);
    return entityId;
  }
}
