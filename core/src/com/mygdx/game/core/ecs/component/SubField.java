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
  private @NonNull IntArray buildings = new IntArray();

  @Override
  protected void reset() {
    this.parent = -0xC0FEE;
    this.buildings.clear();
  }
}
