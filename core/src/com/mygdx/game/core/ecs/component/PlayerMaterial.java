package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import com.mygdx.game.core.model.MaterialBase;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerMaterial extends PooledComponent {

  private MaterialBase material = null;
  private int value = 0;

  @Override
  protected void reset() {
    material = null;
    value = 0;
  }
}
