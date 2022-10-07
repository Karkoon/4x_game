package com.mygdx.game.core.ecs.component;

import com.artemis.Component;
import lombok.Data;

@Data
public class CanAttack extends Component {

  private boolean canAttack;
}
