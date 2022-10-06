package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class SubField extends PooledComponent {

  private int parent = -0xC0FEE;
  private int building = -0xC0FEE;

  @Override
  protected void reset() {
    this.parent = -0xC0FEE;
    this.building = -0xC0FEE;
  }
}
