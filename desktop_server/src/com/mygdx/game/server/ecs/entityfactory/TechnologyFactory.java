package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.TechnologyConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class TechnologyFactory implements EntityFactory<TechnologyConfig> {

  private final ComponentFactory componentFactory;

  @Inject
  public TechnologyFactory(
      @NonNull ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  @Override
  public void createEntity(int entityId, @NonNull TechnologyConfig config) {
    componentFactory.setUpEntityConfig(config, entityId);
  }

}
