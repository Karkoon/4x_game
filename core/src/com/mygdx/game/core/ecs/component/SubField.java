package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class SubField extends PooledComponent {
  private @NonNull Integer parent = 0;

  @Override
  protected void reset() {
    this.parent = 0;
  }
}
