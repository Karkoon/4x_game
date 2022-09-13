package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;

@Data
public class Stats extends PooledComponent {
  private int maxHp;
  private int hp;
  private int attackPower;
  private int defense;
  private int sightRadius;
  private int speed;
  private int moveRange;

  @Override
  public void reset() {
    this.hp = this.maxHp;
  }
}
