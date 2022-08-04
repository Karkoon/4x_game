package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class PlayerToken extends PooledComponent {

  @NonNull
  private String token; // TODO change to static string as in Name

  @Override
  protected void reset() {
    token = null;
  }
}
