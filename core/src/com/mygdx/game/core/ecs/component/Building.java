package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Building extends PooledComponent {

  private @NonNull int parent = -0xC0FEE;

  @Override
  protected void reset() {
    this.parent = -0xC0FEE;
  }
}
