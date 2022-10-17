package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client.ecs.component.Highlighted;
import com.mygdx.game.client.model.ChosenConfig;
import com.mygdx.game.client.ui.CanNotBuildDialogFactory;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.service.BuildingService;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Map;

@GameInstanceScope
@Log
@All({Highlighted.class, SubField.class})
public class BuildSystem extends IteratingSystem {

  @AspectDescriptor(all = {PlayerMaterial.class})
  private EntitySubscription playerMaterialSubscriber;

  private final BuildingService buildingService;
  private final CanNotBuildDialogFactory buildDialog;
  private final ChosenConfig chosenConfig;
  private final GameConfigAssets assets;
  private ComponentMapper<Highlighted> highlightedMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<PlayerMaterial> playerMaterialMapper;

  @Inject
  public BuildSystem(
      GameConfigAssets assets,
      CanNotBuildDialogFactory canNotBuildDialogFactory,
      ChosenConfig chosenConfig,
      BuildingService buildingService
  ) {
    this.assets = assets;
    this.buildDialog = canNotBuildDialogFactory;
    this.chosenConfig = chosenConfig;
    this.buildingService = buildingService;
  }

  @Override
  protected void process(int entityId) {
    if (chosenConfig.isAnyChosen() && chosenConfig.peekClass().equals(BuildingConfig.class)) {
      log.info("some are chosen and there's a sub highlighted entity");
      long buildingConfigId = chosenConfig.pop();
      var config = assets.getGameConfigs().get(BuildingConfig.class, buildingConfigId);
      if (checkIfCanBuy(config.getMaterials(), playerMaterialSubscriber.getEntities())) {
        var targetCoordinate = coordinatesMapper.get(entityId);
        buildingService.createBuilding(buildingConfigId, entityId, targetCoordinate);
      } else {
        buildDialog.createAndShow("You can't build, not enough materials");
      }
      highlightedMapper.remove(entityId);
    }
  }

  public boolean checkIfCanBuy(Map<MaterialBase, MaterialUnit> materials, IntBag entities) {
    for (int i = 0; i < entities.size(); i++) {
      int entityId = entities.get(i);
      var playerMaterial = playerMaterialMapper.get(entityId);
      if (materials.containsKey(playerMaterial.getMaterial())) {
        var material = materials.get(playerMaterial.getMaterial());
        if (material.getAmount() > playerMaterial.getValue())
          return false;
      }
    }
    return true;
  }
}
