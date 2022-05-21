package com.mygdx.game.client.ecs.component;

import com.badlogic.ashley.core.Component;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Name implements Component {

  @NonNull
  private String name;
  @NonNull
  private String polishName;
}
