package com.mygdx.game.bot.util;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.bot.model.ChosenBotType;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.client_core.network.service.BuildingService;
import com.mygdx.game.client_core.util.MaterialUtilClient;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.model.BotType;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Random;

@GameInstanceScope
@Log
public class BotBuildUtil {

  private final float SHOULD_BUILD_RANDOM_FIRST = 0.2f;

  private final BuildingService buildingService;
  private final ChosenBotType chosenBotType;
  private final GameConfigAssets gameConfigAssets;
  private final MaterialUtilClient materialUtilClient;
  private final NetworkWorldEntityMapper networkWorldEntityMapper;
  private final Random random;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;

  @AspectDescriptor(all = {PlayerMaterial.class})
  private EntitySubscription playerMaterialSubscriber;

  @Inject
  public BotBuildUtil (
      BuildingService buildingService,
      ChosenBotType chosenBotType,
      GameConfigAssets gameConfigAssets,
      MaterialUtilClient materialUtilClient,
      NetworkWorldEntityMapper networkWorldEntityMapper,
      World world
  ) {
    this.buildingService = buildingService;
    this.chosenBotType = chosenBotType;
    this.gameConfigAssets = gameConfigAssets;
    this.materialUtilClient = materialUtilClient;
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    this.random = new Random();
    world.inject(this);
  }

  public void build(int fieldEntityId) {
    if (chosenBotType.getBotType() == BotType.RANDOM_FIRST) {
      if (propabilityCheck(SHOULD_BUILD_RANDOM_FIRST)) {
        var buildings = gameConfigAssets.getGameConfigs().getAll(BuildingConfig.class);
        int id = randomBetween(0, buildings.size);
        var buildingConfig = buildings.get(id);
        if (materialUtilClient.checkIfCanBuy(buildingConfig.getMaterials(), playerMaterialSubscriber.getEntities())) {
          var field = fieldMapper.get(fieldEntityId);
          var subFields = field.getSubFields();
          for (int i = 0; i < subFields.size; i++) {
            int sufieldNetworkEntityId = subFields.get(i);
            int subfieldWorldEntityId = networkWorldEntityMapper.getWorldEntity(sufieldNetworkEntityId);
            if (underConstructionMapper.has(subfieldWorldEntityId)) {
              log.info("Create building " + buildingConfig.getId() + " on field " + fieldEntityId);
              buildingService.createBuilding(buildingConfig.getId(), subfieldWorldEntityId, coordinatesMapper.get(subfieldWorldEntityId));
            }
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
