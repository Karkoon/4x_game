package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.IntArray;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Slot extends PooledComponent {

  private IntArray entities = new IntArray();

  @Override
  public void reset() {
    entities.clear();
  }
}
