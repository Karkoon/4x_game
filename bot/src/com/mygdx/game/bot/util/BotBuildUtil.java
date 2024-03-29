package com.mygdx.game.bot.util;

import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.bot.model.ChosenBotType;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.service.BuildingService;
import com.mygdx.game.client_core.util.MaterialUtilClient;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.model.BotType;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Random;

@GameInstanceScope
@Log
public class BotBuildUtil {

  private final float SHOULD_BUILD_RANDOM_FIRST = 0.1f;

  private final BuildingService buildingService;
  private final ChosenBotType chosenBotType;
  private final GameConfigAssets gameConfigAssets;
  private final MaterialUtilClient materialUtilClient;
  private final Random random;

  @AspectDescriptor(all = {PlayerMaterial.class})
  private EntitySubscription playerMaterialSubscriber;

  @Inject
  public BotBuildUtil (
      BuildingService buildingService,
      ChosenBotType chosenBotType,
      GameConfigAssets gameConfigAssets,
      MaterialUtilClient materialUtilClient,
      World world
  ) {
    this.buildingService = buildingService;
    this.chosenBotType = chosenBotType;
    this.gameConfigAssets = gameConfigAssets;
    this.materialUtilClient = materialUtilClient;
    this.random = new Random();
    world.inject(this);
  }

  public boolean build(int fieldEntityId) {
    if (chosenBotType.getBotType() == BotType.RANDOM_FIRST || chosenBotType.getBotType() == BotType.TRAINED) {
      if (probabilityCheck(SHOULD_BUILD_RANDOM_FIRST)) {
        var buildings = gameConfigAssets.getGameConfigs().getAll(BuildingConfig.class);
        int id = randomBetween(0, buildings.size);
        var buildingConfig = buildings.get(id);
        if (materialUtilClient.checkIfCanBuy(buildingConfig.getMaterials(), playerMaterialSubscriber.getEntities())) {
          buildingService.createBuilding(buildingConfig.getId(), fieldEntityId);
          log.info("Create building " + buildingConfig.getId() + " on field " + fieldEntityId);
          return true;
        }
      }
    }
    return false;
  }

  public boolean probabilityCheck(float value) {
    float probabilityValue = random.nextFloat();
    return probabilityValue <= value;
  }

  public int randomBetween(int min, int max) {
    return random.nextInt(max - min) + min;
  }
}
