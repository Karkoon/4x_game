package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.Config;
import lombok.NonNull;

public interface EntityFactory<T extends Config> {

  @NonNull void createEntity(int entityId, T entityConfig);

}
