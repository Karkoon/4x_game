package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;

@Data
public class EntityConfigId extends PooledComponent {
  private int id;

  @Override
  protected void reset() {
    id = 0;
  }
}
