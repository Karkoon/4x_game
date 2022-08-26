package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.di.GameInstanceScope;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class UnitFactory {

  private final ComponentFactory componentFactory;

  @Inject
  public UnitFactory(
      @NonNull ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  public void createEntity(@NonNull UnitConfig config, Coordinates coordinates) {
    int entityId = componentFactory.createEntityId();
    componentFactory.createCoordinateComponent(coordinates, entityId);
    componentFactory.setUpEntityConfig(config, entityId);
  }

}
