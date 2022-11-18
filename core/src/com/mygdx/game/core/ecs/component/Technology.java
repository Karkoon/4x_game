package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import com.mygdx.game.core.model.TechnologyImpact;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Technology extends PooledComponent {

  @NonNull
  private TechnologyImpact impact;

  @Override
  protected void reset() {
    impact = null;
  }
}
