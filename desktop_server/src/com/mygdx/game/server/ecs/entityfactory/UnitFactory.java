package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class UnitFactory implements EntityFactory<UnitConfig> {

  private final ComponentFactory componentFactory;

  @Inject
  public UnitFactory(
      @NonNull ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  @Override
  public void createEntity(int entityId, @NonNull UnitConfig config) {
    componentFactory.setUpEntityConfig(config, entityId);
  }

}
