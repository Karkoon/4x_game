package com.mygdx.game.client.ecs.component;

import com.badlogic.ashley.core.Component;
import lombok.Data;

@Data
public class Id implements Component {
  private final long id;
}
