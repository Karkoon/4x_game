package com.mygdx.game.core.ecs.component;


import com.artemis.Component;
import lombok.Data;

@Data
public class InRecruitment extends Component {

  private long unitConfigId;
  private int turnLeft;
  private String clientToken;
}
