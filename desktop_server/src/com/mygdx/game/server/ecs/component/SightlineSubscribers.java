package com.mygdx.game.server.ecs.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.Bits;
import lombok.Data;

@Data
public class SightlineSubscribers extends Component {
  private Bits clients = new Bits();
  private int sightlineRadius = 1;

  public void setClient(int index) {
    clients.set(index);
  }
}
