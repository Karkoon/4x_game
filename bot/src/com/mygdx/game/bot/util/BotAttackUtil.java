package com.mygdx.game.bot.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.bot.model.ChosenBotType;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.service.AttackEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.model.BotType;
import com.mygdx.game.bot.hud.UnitUtil;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Random;

@GameInstanceScope
@Log
public class BotAttackUtil {

  private final float SHOULD_ATTACK_RANDOM_FIRST = 0.5f;

  private final AttackEntityService attackEntityService;
  private final ChosenBotType chosenBotType;
  private final PlayerInfo playerInfo;
  private final UnitUtil unitUtil;
  private final Random random;
  private final World world;

  @AspectDescriptor(all = {Stats.class, Owner.class, Coordinates.class})
  private EntitySubscription unitsSubscription;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Stats> statsMapper;

  @Inject
  public BotAttackUtil (
      AttackEntityService attackEntityService,
      ChosenBotType chosenBotType,
      UnitUtil unitUtil,
      PlayerInfo playerInfo,
      World world
  ) {
    this.attackEntityService = attackEntityService;
    this.chosenBotType = chosenBotType;
    this.unitUtil = unitUtil;
    this.playerInfo = playerInfo;
    this.random = new Random();
    this.world = world;
    world.inject(this);
  }

  public void attack(int unitId) {
    if (chosenBotType.getBotType() == BotType.RANDOM_FIRST) {
      if (propabilityCheck(SHOULD_ATTACK_RANDOM_FIRST)) {
        IntArray unitsInRange = unitUtil.getUnitsInRange(unitId);
        if (unitsInRange == null || unitsInRange.isEmpty()){
          return;
        }
        int enemyUnitId = unitsInRange.random();
        log.info("Unit " + unitId + " attack " + enemyUnitId);
        attackEntityService.attack(unitId, enemyUnitId);
      }
    }
  }

  public boolean propabilityCheck(float value) {
    float probabilityValue = random.nextFloat();
    return value <= probabilityValue;
  }
}
