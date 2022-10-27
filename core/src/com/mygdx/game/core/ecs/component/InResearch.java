package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InResearch extends PooledComponent {

  private int configRequiredScience = 0;
  private int scienceLeft = 0;

  @Override
  protected void reset() {
    configRequiredScience = 0;
    scienceLeft = 0;
  }
}
