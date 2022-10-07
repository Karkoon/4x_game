package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.server.di.GameInstanceScope;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class AttackEntityService extends WorldService {

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<CanAttack> canAttackMapper;

  private World world;

  @Inject
  public AttackEntityService(
      World world
  ) {
    world.inject(this);
  }

  public void attackEntity(int attacker, int attacked) {
    if (!canAttackMapper.get(attacker).isCanAttack()
        || !coordinatesMapper.get(attacker).equals(coordinatesMapper.get(attacked))) {
      log.info("tried to attack unlawfully");
      return;
    }
    var statsMapper = world.getMapper(Stats.class);
    attackAndCounterAttack(statsMapper.get(attacker), statsMapper.get(attacked));
    canAttackMapper.get(attacker).setCanAttack(false);
    log.info("Attack component");
    setDirty(attacker, CanAttack.class, world);
    setDirty(attacked, Stats.class, world);
    setDirty(attacker, Stats.class, world);
    world.process();
  }

  private void attackAndCounterAttack(Stats attacker, Stats attacked) {
    attack(attacker, attacked);
    if (attacked.getHp() > 0) {
      attack(attacked, attacker);
    }
  }

  private void attack(Stats attacker, Stats attacked) {
    attacked.setHp(attacked.getHp() - Math.max(0, attacker.getAttackPower() - attacked.getDefense()));
  }
}
