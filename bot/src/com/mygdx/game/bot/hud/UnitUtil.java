package com.mygdx.game.bot.hud;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.util.DistanceUtil;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class UnitUtil {

  private final PlayerInfo playerInfo;

  @AspectDescriptor(one = {Movable.class, Owner.class, Stats.class})
  private EntitySubscription playerUnitsSubscriptions;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<CanAttack> canAttackMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Stats> statsMapper;

  @Inject
  public UnitUtil(
      PlayerInfo playerInfo,
      World world
  ) {
    world.inject(this);
    this.playerInfo = playerInfo;
  }

  public IntArray getAllUnits() {
    // Check if unit can move
    IntArray availableUnits = new IntArray();
    for (int i = 0; i < playerUnitsSubscriptions.getEntities().size(); i++) {
      int entityId = playerUnitsSubscriptions.getEntities().get(i);
      var owner = ownerMapper.get(entityId);
      var stats = statsMapper.get(entityId);
      if (playerInfo.getToken().equals(owner.getToken()) && stats != null && stats.getMoveRange() > 0) {
        availableUnits.add(entityId);
      }
    }
    return availableUnits;
  }
  public int selectNextUnit() {
    // Check if unit can move
    for (int i = 0; i < playerUnitsSubscriptions.getEntities().size(); i++) {
      int entityId = playerUnitsSubscriptions.getEntities().get(i);
      var owner = ownerMapper.get(entityId);
      var stats = statsMapper.get(entityId);
      if (playerInfo.getToken().equals(owner.getToken()) && stats != null && stats.getMoveRange() > 0) {
        return entityId;
      }
    }
/*    // Check if unit can attack
    for (int i = 0; i < playerUnitsSubscriptions.getEntities().size(); i++) {
      int entityId = playerUnitsSubscriptions.getEntities().get(i);
      var owner = ownerMapper.get(entityId);
      var canAttack = canAttackMapper.get(entityId);
      if (playerInfo.getToken().equals(owner.getToken()) && canAttack != null && canAttack.isCanAttack()) {
        return entityId;
      }
    }*/
    return 0xC0FFEE;
  }

  public IntArray getUnitsInRange(int allyUnit){
    if (!canAttackMapper.has(allyUnit)){
      return null;
    }

    var unitsInRange = new IntArray();
    for (int i = 0; i < playerUnitsSubscriptions.getEntities().size(); i++) {
      int tmpUnit = playerUnitsSubscriptions.getEntities().get(i);
      if (canAttack(allyUnit, tmpUnit)) {
        unitsInRange.add(tmpUnit);
      }
    }

    if (unitsInRange.isEmpty()){
      return null;
    }

    return unitsInRange;
  }

  private boolean canAttack(int allyUnit, int enemyUnit) {
    if (playerInfo.getToken().equals(ownerMapper.get(enemyUnit).getToken())){
      return false;
    }
    var allyCoordinates = coordinatesMapper.get(allyUnit);
    var enemyCoordinates = coordinatesMapper.get(enemyUnit);
    var distance = DistanceUtil.distance(allyCoordinates, enemyCoordinates);
    var range = statsMapper.get(allyUnit).getAttackRange();
    return range >= distance;
  }
}
