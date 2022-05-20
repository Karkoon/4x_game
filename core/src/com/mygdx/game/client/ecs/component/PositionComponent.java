package com.mygdx.game.client.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import lombok.Data;
import lombok.NonNull;

@Data
public class PositionComponent implements Component, Pool.Poolable {

  private @NonNull Vector3 position = new Vector3();

  @Override
  public void reset() {
    position.set(Vector3.Zero);
  }
}
