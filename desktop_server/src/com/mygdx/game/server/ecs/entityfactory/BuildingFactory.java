package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.World;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.server.di.GameInstanceScope;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class BuildingFactory {

  private final World world;
  private final ComponentFactory componentFactory;

  @Inject
  public BuildingFactory(
      World world,
      ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
    this.world = world;
    this.world.inject(this);
  }

  public void createBeforeEntity(
      @NonNull BuildingConfig config,
      @NonNull Coordinates coordinates,
      int parentSubfield, int clientIndex
  ) {
    int entityId = componentFactory.createEntityId();
    log.info("Create construction building: " + entityId + " for subfield: " + parentSubfield);
    componentFactory.createCoordinateComponent(coordinates, entityId);
    componentFactory.createNameComponent(entityId, "building construction " + config.getName() + " " + entityId + " " + parentSubfield);
    componentFactory.createUnderConstruction(config, entityId, parentSubfield);
    componentFactory.createChangeSubscribersComponentFast(entityId, clientIndex);
    componentFactory.createFriendlyOrFoeComponent(entityId, null); // todo add client to friendlies?
    componentFactory.createSharedComponents(entityId,
            new Class[]{Coordinates.class, UnderConstruction.class},
            new Class[]{Coordinates.class, UnderConstruction.class});
  }

  public void createEntity(
      int entityId,
      @NonNull BuildingConfig config,
      int parentSubfield
  ) {

    log.info("Create building: " + entityId + " for subfield: " + parentSubfield);
    componentFactory.setUpEntityConfig(config, entityId);
    componentFactory.createNameComponent(entityId, "building " + config.getName() + " " + entityId + " " + parentSubfield);
    componentFactory.createBuildingComponent(entityId, parentSubfield);
    componentFactory.createSharedComponents(entityId,
            new Class[]{Coordinates.class, EntityConfigId.class, Building.class},
            new Class[]{Coordinates.class, EntityConfigId.class, Building.class});
  }

}
