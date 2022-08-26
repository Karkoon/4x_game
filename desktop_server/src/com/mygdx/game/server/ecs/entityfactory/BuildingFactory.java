package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.server.di.GameInstanceScope;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class BuildingFactory {

  private final ComponentFactory componentFactory;

  @Inject
  public BuildingFactory(
      @NonNull ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  public void createEntity(
      @NonNull BuildingConfig config,
      @NonNull Coordinates coordinates,
      int parentField
  ) {
    int entityId = componentFactory.createEntityId();
    componentFactory.createCoordinateComponent(coordinates, entityId);
    componentFactory.setUpEntityConfig(config, entityId);
    componentFactory.createNameComponent(entityId, "building " + config.getName() + " " + entityId + " " + parentField);
    componentFactory.createBuildingComponent(entityId, parentField);
    componentFactory.createChangeSubscribersComponent(entityId);
    componentFactory.createFriendlyOrFoeComponent(entityId, null);
    componentFactory.createSharedComponents(entityId,
            new Class[]{Coordinates.class, EntityConfigId.class, Building.class},
            new Class[]{Coordinates.class, EntityConfigId.class, Building.class});
  }

}
