package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.SubFieldConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class SubFieldFactory implements EntityFactory<SubFieldConfig> {

  private final ComponentFactory componentFactory;

  @Inject
  public SubFieldFactory(
      @NonNull ComponentFactory componentFactory
  ) {
    this.componentFactory = componentFactory;
  }

  @Override
  public void createEntity(int entityId, @NonNull SubFieldConfig config) {
    componentFactory.setUpEntityConfig(config, entityId);
  }

}
