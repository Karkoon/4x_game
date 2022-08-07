package com.mygdx.game.server.ecs.entityfactory;

import com.mygdx.game.config.EntityConfig;
import lombok.NonNull;

public interface EntityFactory<T extends EntityConfig> {

  @NonNull void createEntity(int entityId, T entityConfig);

}
