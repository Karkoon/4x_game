package com.mygdx.game.client.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import lombok.Data;

@Data
public class StatsComponent implements Component, Pool.Poolable {
  int maxHp;
  int hp;
  int attackPower;
  int defense;
  int sightRadius;
  int speed;

  @Override
  public void reset() {
    this.hp = maxHp;
  }
}
