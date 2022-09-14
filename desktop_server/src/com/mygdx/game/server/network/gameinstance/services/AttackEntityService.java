package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.World;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Stats;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class AttackEntityService extends WorldService {

  @Inject
  public AttackEntityService() {
    super();
  }

  public void attackEntity(int attacker, int attacked, World world) {
    var coordinatesMapper = world.getMapper(Coordinates.class);
    var canAttackMapper = world.getMapper(CanAttack.class);
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
