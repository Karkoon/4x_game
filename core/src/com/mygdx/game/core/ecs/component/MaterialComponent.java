package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import com.mygdx.game.core.model.MaterialBase;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MaterialComponent  extends PooledComponent {

  private MaterialBase material = null;
  private int value = 0;

  public void add(int valueToAdd) {
    this.value += valueToAdd;
  }

  public boolean remove(int valueToRemove) {
    if (valueToRemove > this.value)
      return false;
    this.value -= valueToRemove;
    return true;
  }

  @Override
  protected void reset() {
    material = null;
    value = 0;
  }
}
