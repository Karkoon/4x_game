package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.MaterialComponent;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class MaterialFactory {

  private final ComponentFactory componentFactory;

  @Inject
  public MaterialFactory(
      @NonNull ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  public void createEntity(MaterialBase materialBase, Client client) {
    int entityId = componentFactory.createEntityId();
    componentFactory.createNameComponent(entityId, "material " + materialBase.name() + " " + entityId);
    componentFactory.createMaterialComponent(entityId, materialBase);
    componentFactory.createChangeSubscribersComponent(entityId);
    componentFactory.createOwnerComponent(entityId, client);
    componentFactory.createFriendlyOrFoeComponent(entityId, client);
    var componentsToSend = new Class[]{MaterialComponent.class};
    componentFactory.createDirtyComponent(entityId, componentsToSend);
    componentFactory.createSharedComponents(entityId,
      componentsToSend,
      new Class[]{}
    );
  }
}
