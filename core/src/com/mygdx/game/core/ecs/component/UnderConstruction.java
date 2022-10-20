package com.mygdx.game.core.ecs.component;

import com.artemis.Component;
import lombok.Data;

@Data
public class UnderConstruction extends Component {

  private int buildingConfigId;
  private int turnLeft;
  private int parentSubfield;
  private int clientIndex;
}
