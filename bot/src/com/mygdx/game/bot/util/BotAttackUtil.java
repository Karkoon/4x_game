package com.mygdx.game.bot.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.bot.model.ChosenBotType;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.service.AttackEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.model.BotType;
import com.mygdx.game.core.util.DistanceUtil;
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
  private final Random random;

  @AspectDescriptor(all = {Movable.class, Owner.class, Coordinates.class})
  private EntitySubscription unitsSubscription;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Stats> statsMapper;

  @Inject
  public BotAttackUtil (
      AttackEntityService attackEntityService,
      ChosenBotType chosenBotType,
      PlayerInfo playerInfo,
      World world
  ) {
    this.attackEntityService = attackEntityService;
    this.chosenBotType = chosenBotType;
    this.playerInfo = playerInfo;
    this.random = new Random();
    world.inject(this);
  }

  public void attack(int unitId) {
    if (chosenBotType.getBotType() == BotType.RANDOM_FIRST || chosenBotType.getBotType() == BotType.TRAINED) {
      if (propabilityCheck(SHOULD_ATTACK_RANDOM_FIRST)) {
        var coordinates = coordinatesMapper.get(unitId);
        var stats = statsMapper.get(unitId);
        var entities = unitsSubscription.getEntities();
        for (int i = 0; i < entities.size(); i++) {
          int otherUnitIt = entities.get(i);
          if (haveDifferentOwner(playerInfo.getToken(), otherUnitIt) && DistanceUtil.distance(coordinates, coordinatesMapper.get(otherUnitIt)) <= stats.getAttackRange()) {
            log.info("Unit " + unitId + " attack " + otherUnitIt);
            attackEntityService.attack(unitId, otherUnitIt);
          }
        }
      }
    }
  }

  public boolean propabilityCheck(float value) {
    float probabilityValue = random.nextFloat();
    return probabilityValue <= value;
  }

  private boolean haveDifferentOwner(String thisPlayerToken, int otherEntity) {
    return !thisPlayerToken.equals(ownerMapper.get(otherEntity).getToken());
  }
}
