package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.AppliedTechnologies;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class UnitFactory {

  private final ComponentFactory componentFactory;

  @Inject
  public UnitFactory(
      ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  public void createEntity(
      @NonNull UnitConfig config,
      @NonNull Coordinates coordinates,
      @NonNull Client client
  ) {
    int entityId = componentFactory.createEntityId();
    componentFactory.createAppliedTechnologies(entityId);
    componentFactory.createUnitComponent(entityId);
    componentFactory.createCoordinateComponent(coordinates, entityId);
    componentFactory.setUpEntityConfig(config, entityId);
    componentFactory.createNameComponent(entityId, "unit " + config.getName() + " " + entityId);
    componentFactory.createFriendlyOrFoeComponent(entityId, client);
    componentFactory.createChangeSubscribersComponent(entityId);
    componentFactory.createSightlineSubscribersComponent(entityId, config.getSightRadius());
    componentFactory.createStatsComponent(entityId, config);
    componentFactory.createOwnerComponent(entityId, client);
    componentFactory.createCanAttackComponent(entityId);
    var componentsToSend = new Class[]{Coordinates.class, Stats.class, EntityConfigId.class, Owner.class, CanAttack.class, AppliedTechnologies.class};
    componentFactory.createDirtyComponent(entityId, componentsToSend);
    componentFactory.createSharedComponents(entityId,
        componentsToSend,
        new Class[]{Coordinates.class, EntityConfigId.class, Owner.class}
    );
  }

}
