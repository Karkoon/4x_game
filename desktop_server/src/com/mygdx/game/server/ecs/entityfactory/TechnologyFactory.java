package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.server.di.GameInstanceScope;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@GameInstanceScope
@Log
public class TechnologyFactory {

  private final ComponentFactory componentFactory;

  @Inject
  public TechnologyFactory(
      @NonNull ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  public void createEntity(@NonNull TechnologyConfig config) {
    int entityId = componentFactory.createEntityId();
    componentFactory.setUpEntityConfig(config, entityId);
  }

}
