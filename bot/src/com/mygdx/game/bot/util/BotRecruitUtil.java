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
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.InRecruitment;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.model.BotType;
import com.mygdx.game.core.model.BuildingImpactValue;
import com.mygdx.game.core.model.BuildingType;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.ArrayList;
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
    world.inject(this);
  }

  public void recruitUnit(int fieldEntityId) {
    if (chosenBotType.getBotType() == BotType.RANDOM_FIRST) {
      if (propabilityCheck(SHOULD_RECRUIT_RANDOM_FIRST)) {
        var availableUnits = new ArrayList<>();
        var field = fieldMapper.get(fieldEntityId);
        var subFields = field.getSubFields();
        for (int i = 0; i < subFields.size; i++) {
          int subfieldEntityId = subFields.get(i);
          int sufieldWorldEntityId = networkWorldEntityMapper.getWorldEntity(subfieldEntityId);
          var subField = subfieldMapper.get(sufieldWorldEntityId);
          int buildingEntityId = subField.getBuilding();
          if (buildingEntityId != -0xC0FEE) {
            var buildingConfig = gameConfigAssets.getGameConfigs().get(
              BuildingConfig.class,
              entityConfigIdMapper.get(buildingEntityId).getId()
            );
            if (buildingConfig.getImpact().getBuildingType().equals(BuildingType.RECRUITMENT_BUILDING)) {
              for (BuildingImpactValue unitConfigId : buildingConfig.getImpact().getBuildingImpactValues()) {
                var untiConfig = gameConfigAssets.getGameConfigs().get(
                  UnitConfig.class,
                  unitConfigId.getValue()
                );
                if (untiConfig.getCivilizationConfigId() == playerInfo.getCivilization())
                  availableUnits.add(unitConfigId.getValue());
              }
            }
          }
        }

        if (availableUnits.size() > 0) {
          int unitConfigId = randomBetween(0, availableUnits.size());
          if (!inRecruitmentMapper.has(fieldEntityId) &&
              infieldUtil.checkIfEnoughMaterialsToRecruitUnit(unitConfigId)) {
            log.info("Recruit unit " + unitConfigId + " to field " + fieldEntityId);
            createUnitService.createUnit(unitConfigId, fieldEntityId);
          }
        }
      }
    }
  }

  public boolean propabilityCheck(float value) {
    float probabilityValue = random.nextFloat();
    return value <= probabilityValue;
  }

  public int randomBetween(int min, int max) {
    return random.nextInt(max - min) + min;
  }

}
