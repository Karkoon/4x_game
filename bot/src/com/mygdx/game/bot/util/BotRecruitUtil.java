package com.mygdx.game.bot.util;

import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.bot.model.ChosenBotType;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.service.CreateUnitService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.model.BotType;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Random;

@GameInstanceScope
@Log
public class BotRecruitUtil {

  private final float SHOULD_RECRUIT_RANDOM_FIRST = 0.15f;

  private final ChosenBotType chosenBotType;
  private final CreateUnitService createUnitService;
  private final Random random;

  @AspectDescriptor(all = {Stats.class, Owner.class, Coordinates.class})
  private EntitySubscription unitsSubscription;

  @Inject
  public BotRecruitUtil (
      ChosenBotType chosenBotType,
      CreateUnitService createUnitService,
      World world
  ) {
    this.chosenBotType = chosenBotType;
    this.createUnitService = createUnitService;
    this.random = new Random();
    world.inject(this);
  }

  public boolean recruitUnit(int fieldEntityId) {
    if (chosenBotType.getBotType() == BotType.RANDOM_FIRST || chosenBotType.getBotType() == BotType.TRAINED) {
      if (probabilityCheck(SHOULD_RECRUIT_RANDOM_FIRST)) {
        log.info("Recruit unit on field " + fieldEntityId);
        createUnitService.createUnit(fieldEntityId);
        return true;
      }
    }
    return false;
  }

  public boolean probabilityCheck(float value) {
    float probabilityValue = random.nextFloat();
    return probabilityValue <= value;
  }

}
