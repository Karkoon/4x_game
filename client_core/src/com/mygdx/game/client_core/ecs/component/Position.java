package com.mygdx.game.client_core.ecs.component;

import com.artemis.PooledComponent;
import com.artemis.annotations.PooledWeaver;
import com.badlogic.gdx.math.Vector3;
import lombok.Data;
import lombok.NonNull;

@Data
@PooledWeaver
public class Position extends PooledComponent {

  @NonNull
  private final Vector3 value = new Vector3();

  @Override
  public void reset() {
    value.set(Vector3.Zero);
  }
}
