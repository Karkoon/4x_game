package com.mygdx.game.client_core.ecs.entityfactory;

import com.mygdx.game.config.Config;

public interface Setter {
  Result set(Config config, int entityId);

  enum Result {
    HANDLED, HANDLING_ERROR, REJECTED
  }
}
