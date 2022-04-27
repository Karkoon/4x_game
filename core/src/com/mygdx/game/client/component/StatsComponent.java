package com.mygdx.game.client.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import lombok.Data;

@Data
public class StatsComponent implements Component, Pool.Poolable {
  private int maxHp;
  private int hp;
  private int attackPower;
  private int defense;
  private int sightRadius;
  private int speed;

  @Override
  public void reset() {
    this.hp = this.maxHp;
  }
}
