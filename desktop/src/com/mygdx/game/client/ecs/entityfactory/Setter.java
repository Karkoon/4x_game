package com.mygdx.game.client.ecs.entityfactory;

import com.mygdx.game.config.EntityConfig;

public interface Setter {
  Result set(EntityConfig config, int entityId);

  enum Result {
    HANDLED, NOT_HANDLED, REJECTED
  }
}
