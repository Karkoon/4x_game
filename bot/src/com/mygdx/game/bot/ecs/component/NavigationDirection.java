package com.mygdx.game.bot.ecs.component;

import com.artemis.Component;
import com.mygdx.game.bot.screen.Navigator;
import lombok.Data;

@Data
public class NavigationDirection extends Component {

  public Navigator.Direction direction;
}
