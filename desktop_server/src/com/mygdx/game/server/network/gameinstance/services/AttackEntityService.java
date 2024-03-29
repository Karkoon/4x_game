package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.model.BotType;
import com.mygdx.game.core.network.messages.UnitAttackedMessage;
import com.mygdx.game.core.util.DistanceUtil;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.MessageSender;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class AttackEntityService extends WorldService {

  private final MessageSender messageSender;
  private World world;

  private ComponentMapper<CanAttack> canAttackMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;


  @Inject
  public AttackEntityService(
      MessageSender messageSender,
      World world
  ) {
    this.messageSender = messageSender;
    world.inject(this);
  }

  public void attackEntity(int attacker, int attacked, Client client) {
    if (!canAttackMapper.get(attacker).isCanAttack()) {
      log.info("tried to attack unlawfully");
      return;
    }
    var statsMapper = world.getMapper(Stats.class);

    Coordinates attakerCoordinates = coordinatesMapper.get(attacker);
    Coordinates attackedCoordinates = coordinatesMapper.get(attacked);

    int attackerAttackRange = statsMapper.get(attacker).getAttackRange();
    int distance = DistanceUtil.distance(attakerCoordinates, attackedCoordinates);

    if (distance > attackerAttackRange){
      log.warning("Attack out of attack range!!!");
    }
    else {
      int attackedAttackRange = statsMapper.get(attacked).getAttackRange();
      if (distance <= attackedAttackRange){
        attackAndCounterAttack(statsMapper.get(attacker), statsMapper.get(attacked));
      }
      else {
        attack(statsMapper.get(attacker), statsMapper.get(attacked));
      }
    }
    canAttackMapper.get(attacker).setCanAttack(false);
    log.info("Attack component");
    setDirty(attacker, CanAttack.class, world);
    setDirty(attacker, Stats.class, world);
    setDirty(attacked, Stats.class, world);
    world.process();
    if (client.getBotType() != BotType.NOT_BOT)
      messageSender.send(new UnitAttackedMessage(), client);
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
