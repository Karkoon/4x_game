package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class TechnologyFactory {

  private final ComponentFactory componentFactory;

  @Inject
  public TechnologyFactory(
      ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  public void createEntity(@NonNull TechnologyConfig config, Client client) {
    int entityId = componentFactory.createEntityId();
    componentFactory.setUpEntityConfig(config, entityId);
    componentFactory.createNameComponent(entityId, "technology " + config.getName() + " " + entityId);
    componentFactory.createChangeSubscribersComponent(entityId);
    componentFactory.createOwnerComponent(entityId, client);
    componentFactory.createFriendlyOrFoeComponent(entityId, client);
    var componentsToSend = new Class[]{EntityConfigId.class};
    componentFactory.createDirtyComponent(entityId, componentsToSend);
    componentFactory.createSharedComponents(entityId,
      componentsToSend,
      new Class[]{}
    );
  }

}
