package com.mygdx.game.server.ecs.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.Bits;
import lombok.Data;

@Data
public class ChangeSubscribers extends Component {

  private Bits changedSubscriptionState = new Bits();
  private Bits clients = new Bits();
}
