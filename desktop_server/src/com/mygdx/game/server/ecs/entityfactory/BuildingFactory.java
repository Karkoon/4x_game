package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
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

  private World world;
  private final ComponentFactory componentFactory;

  private ComponentMapper<SubField> subFieldComponentMapper;


  @Inject
  public BuildingFactory(
      World world,
      ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
    this.world = world;
    this.world.inject(this);
  }

  public void createEntity(
      @NonNull BuildingConfig config,
      @NonNull Coordinates coordinates,
      int parentSubfield, int clientIdex
  ) {
    if (subFieldComponentMapper.get(parentSubfield).getBuilding() != -0xC0FEE) {
      log.info("THERE IS BUILDING WITH PARENT: " + parentSubfield);
    } else {
      int entityId = componentFactory.createEntityId();
      log.info("Create building: " + entityId + " for subfield: " + parentSubfield);
      componentFactory.createCoordinateComponent(coordinates, entityId);
      componentFactory.setUpEntityConfig(config, entityId);
      componentFactory.createNameComponent(entityId, "building " + config.getName() + " " + entityId + " " + parentSubfield);
      componentFactory.createBuildingComponent(entityId, parentSubfield);
      componentFactory.createChangeSubscribersComponentFast(entityId, clientIdex);
      componentFactory.createFriendlyOrFoeComponent(entityId, null);
      componentFactory.createSharedComponents(entityId,
              new Class[]{Coordinates.class, EntityConfigId.class, Building.class},
              new Class[]{Coordinates.class, EntityConfigId.class, Building.class});
    }
  }

}
