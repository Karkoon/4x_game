package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client.ecs.component.Highlighted;
import com.mygdx.game.client.model.ChosenConfig;
import com.mygdx.game.client.ui.CanNotBuildDialogFactory;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.service.BuildingService;
import com.mygdx.game.client_core.util.MaterialUtilClient;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.ecs.component.SubField;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Highlighted.class, SubField.class})
@GameInstanceScope
@Log
public class BuildSystem extends IteratingSystem {


  private final BuildingService buildingService;
  private final CanNotBuildDialogFactory canNotBuildDialogFactory;
  private final ChosenConfig chosenConfig;
  private final GameConfigAssets gameConfigAssets;
  private final Lazy<MaterialUtilClient> materialUtilClient;

  @AspectDescriptor(all = {PlayerMaterial.class})
  private EntitySubscription playerMaterialSubscriber;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Highlighted> highlightedMapper;

  @Inject
  public BuildSystem(
      BuildingService buildingService,
      CanNotBuildDialogFactory canNotBuildDialogFactory,
      ChosenConfig chosenConfig,
      GameConfigAssets gameConfigAssets,
      Lazy<MaterialUtilClient> materialUtilClient
  ) {
    this.buildingService = buildingService;
    this.canNotBuildDialogFactory = canNotBuildDialogFactory;
    this.chosenConfig = chosenConfig;
    this.gameConfigAssets = gameConfigAssets;
    this.materialUtilClient = materialUtilClient;
  }

  @Override
  protected void process(int entityId) {
    if (chosenConfig.isAnyChosen() && chosenConfig.peekClass().equals(BuildingConfig.class)) {
      log.info("some are chosen and there's a sub highlighted entity");
      long buildingConfigId = chosenConfig.pop();
      var config = gameConfigAssets.getGameConfigs().get(BuildingConfig.class, buildingConfigId);
      if (materialUtilClient.get().checkIfCanBuy(config.getMaterials(), playerMaterialSubscriber.getEntities())) {
        var targetCoordinate = coordinatesMapper.get(entityId);
        buildingService.createBuilding(buildingConfigId, entityId, targetCoordinate);
      } else {
        canNotBuildDialogFactory.createAndShow("You can't build, not enough materials");
      }
      highlightedMapper.remove(entityId);
    }
  }
}
