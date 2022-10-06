package com.mygdx.game.core.ecs.component;

import com.artemis.Component;
import lombok.Data;

@Data
public class Owner extends Component {

  private String token = null;
}
