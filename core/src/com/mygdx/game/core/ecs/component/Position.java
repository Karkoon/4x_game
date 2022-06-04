package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import com.artemis.annotations.PooledWeaver;
import com.badlogic.gdx.math.Vector3;
import lombok.Data;
import lombok.NonNull;

@Data
@PooledWeaver
public class Position extends PooledComponent {

  private @NonNull Vector3 position = new Vector3();

  @Override
  public void reset() {
    position.set(Vector3.Zero);
  }

  public void set(Position other) {
    this.getPosition().set(other.getPosition());
  }
}
