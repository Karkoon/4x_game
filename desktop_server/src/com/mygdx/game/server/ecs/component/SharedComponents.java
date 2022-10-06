package com.mygdx.game.server.ecs.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.Bits;
import lombok.Data;

@Data
public class SharedComponents extends Component {

  private Bits foes = new Bits();
  private Bits friendlies = new Bits(); // categories helping to distinguish the components to send
}
