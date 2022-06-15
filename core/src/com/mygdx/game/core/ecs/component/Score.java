package com.mygdx.game.client.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Score implements Component, Pool.Poolable {

  private Integer scoreValue;

  @Override
  public void reset() {
    scoreValue = 0;
  }
}
