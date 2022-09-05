package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Name extends PooledComponent {

  @NonNull
  private String name;
  @NonNull
  private String polishName;

  @Override
  protected void reset() {
    name = null; // TODO: 03.06.2022 change both to static "" string
    polishName = null;
  }
}
