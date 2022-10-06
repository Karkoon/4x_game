package com.mygdx.game.client.ecs.component;

import com.artemis.Component;
import com.mygdx.game.client.screen.Navigator;
import lombok.Data;

@Data
public class NavigationDirection extends Component {

  public Navigator.Direction direction;
}
