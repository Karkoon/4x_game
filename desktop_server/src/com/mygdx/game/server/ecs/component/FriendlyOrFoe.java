package com.mygdx.game.server.ecs.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.Bits;
import lombok.Data;

@Data
public class FriendlyOrFoe extends Component {
  private Bits friendlies = new Bits(); // 1 friendly; 0 foe
}
