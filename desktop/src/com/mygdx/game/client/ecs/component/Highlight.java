package com.mygdx.game.client.ecs.component;

import com.artemis.Component;
import lombok.Data;

@Data
public class Highlight extends Component {
  private boolean inField = false;
}
