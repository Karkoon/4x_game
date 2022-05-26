package com.mygdx.game.client.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Slot implements Component, Pool.Poolable {

  private Array<Entity> entities = new Array<>();

  @Override
  public void reset() {
    entities.clear();
  }
}
