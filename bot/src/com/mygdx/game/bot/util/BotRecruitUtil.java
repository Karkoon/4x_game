package com.mygdx.game.bot.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.bot.model.ChosenBotType;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.client_core.network.service.CreateUnitService;
import com.mygdx.game.client_core.util.InfieldUtil;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.InRecruitment;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.SubField;
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
  private final GameConfigAssets gameConfigAssets;
  private final InfieldUtil infieldUtil;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;
  private final PlayerInfo playerInfo;
  private final Random random;
  private final World world;

  @AspectDescriptor(all = {Stats.class, Owner.class, Coordinates.class})
  private EntitySubscription unitsSubscription;

  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<InRecruitment> inRecruitmentMapper;
  private ComponentMapper<SubField> subfieldMapper;

  @Inject
  public BotRecruitUtil (
      ChosenBotType chosenBotType,
      CreateUnitService createUnitService,
      GameConfigAssets gameConfigAssets,
      InfieldUtil infieldUtil,
      NetworkWorldEntityMapper networkWorldEntityMapper,
      PlayerInfo playerInfo,
      World world
  ) {
    this.chosenBotType = chosenBotType;
    this.createUnitService = createUnitService;
    this.gameConfigAssets = gameConfigAssets;
    this.infieldUtil = infieldUtil;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    this.playerInfo = playerInfo;
    this.random = new Random();
    this.world = world;
    world.inject(this);
  }

  public boolean recruitUnit(int fieldEntityId) {
    if (chosenBotType.getBotType() == BotType.RANDOM_FIRST) {
      if (propabilityCheck(SHOULD_RECRUIT_RANDOM_FIRST)) {
        log.info("Recruit unit on field " + fieldEntityId);
        createUnitService.createUnit(fieldEntityId);
        return true;
      }
    }
    return false;
  }

  public boolean propabilityCheck(float value) {
    float probabilityValue = random.nextFloat();
    return probabilityValue <= value;
  }

}
