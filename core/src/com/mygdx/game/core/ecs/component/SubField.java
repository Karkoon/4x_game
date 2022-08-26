package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.utils.IntArray;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class SubField extends PooledComponent {

  private @NonNull int parent = -0xC0FEE;
  private @NonNull int building = -0xC0FEE;

  @Override
  protected void reset() {
    this.parent = -0xC0FEE;
    this.building = -0xC0FEE;
  }
}
