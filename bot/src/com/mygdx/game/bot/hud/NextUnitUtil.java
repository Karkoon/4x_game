package com.mygdx.game.bot.hud;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Log
public class NextUnitUtil {

  private final PlayerInfo playerInfo;

  @AspectDescriptor(one = {Movable.class, Owner.class, Stats.class})
  private EntitySubscription playerUnitsSubscriptions;

  private ComponentMapper<CanAttack> canAttackMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Stats> statsMapper;

  @Inject
  public NextUnitUtil(
      PlayerInfo playerInfo,
      World world
  ) {
    world.inject(this);
    this.playerInfo = playerInfo;
  }

  public ArrayList<Integer> selectMoveUnits() {
    var movableUnits = new ArrayList<Integer>();
    for (int i = 0; i < playerUnitsSubscriptions.getEntities().size(); i++) {
      int entityId = playerUnitsSubscriptions.getEntities().get(i);
      var owner = ownerMapper.get(entityId);
      var stats = statsMapper.get(entityId);
      if (playerInfo.getToken().equals(owner.getToken()) && stats != null && stats.getMoveRange() > 0) {
        movableUnits.add(entityId);
      }
    }
    return movableUnits;
  }

  public List<Integer> getUnitsToAttack() {
    var attackUnits = new ArrayList<Integer>();
    for (int i = 0; i < playerUnitsSubscriptions.getEntities().size(); i++) {
      int entityId = playerUnitsSubscriptions.getEntities().get(i);
      var owner = ownerMapper.get(entityId);
      var canAttack = canAttackMapper.get(entityId);
      if (playerInfo.getToken().equals(owner.getToken()) && canAttack != null && canAttack.isCanAttack()) {
        attackUnits.add(entityId);
      }
    }
    return attackUnits;
  }
}
