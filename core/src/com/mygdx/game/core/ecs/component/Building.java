package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Building extends PooledComponent {

  private int parent = -0xC0FEE;

  @Override
  protected void reset() {
    this.parent = -0xC0FEE;
  }
}
