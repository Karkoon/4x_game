package com.mygdx.game.core.ecs.component;

import com.artemis.Component;
import lombok.Data;

@Data
public class UnderConstruction extends Component {

  public static String constructionTexture = "buildings/construction.png";

  private int buildingEntityId;
  private int turnLeft;
  private int parentSubfield;
  private int clientIndex;
}
