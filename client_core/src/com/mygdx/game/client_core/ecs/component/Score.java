package com.mygdx.game.client_core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Score extends PooledComponent {

  private int scoreValue;

  @Override
  public void reset() {
    scoreValue = 0;
  }
}
