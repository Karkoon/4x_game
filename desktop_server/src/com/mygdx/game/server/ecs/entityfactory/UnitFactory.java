package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class UnitFactory {

  private final ComponentFactory componentFactory;

  @Inject
  public UnitFactory(
      @NonNull ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  public int createEntity(@NonNull UnitConfig config, Coordinates coordinates) {
    int entityId = componentFactory.createEntityId();
    componentFactory.createCoordinateComponent(coordinates, entityId);
    componentFactory.setUpEntityConfig(config, entityId);
    return entityId;
  }
}
