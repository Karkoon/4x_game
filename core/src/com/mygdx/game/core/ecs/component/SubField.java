package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubField extends PooledComponent {

  private int building = -0xC0FEE;
  private int parent = -0xC0FEE;

  @Override
  protected void reset() {
    this.building = -0xC0FEE;
    this.parent = -0xC0FEE;
  }

  public boolean hasBuilding() {
    return building != -0xC0FEE;
  }
}
