package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
public class Stats extends PooledComponent {

  private int maxHp;
  private int hp;
  private int attackPower;
  private int defense;
  private int sightRadius;
  private int attackRange;
  private int moveRange;
  private int maxMoveRange;

  @Override
  public void reset() {
    this.hp = this.maxHp;
  }

  public void set(Stats component) {
    maxHp = component.getMaxHp();
    hp = component.getHp();
    attackPower = component.getAttackPower();
    defense = component.getDefense();
    sightRadius = component.getSightRadius();
    attackRange = component.getAttackRange();
    moveRange = component.getMoveRange();
    maxMoveRange = component.getMaxMoveRange();
    log.info("current HP: " + hp);
  }
}
