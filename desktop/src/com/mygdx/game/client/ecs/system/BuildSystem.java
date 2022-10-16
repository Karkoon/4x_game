package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ecs.component.Highlighted;
import com.mygdx.game.client.model.ChosenConfig;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.service.BuildingService;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
@All({Highlighted.class, SubField.class})
public class BuildSystem extends IteratingSystem {

  private final ChosenConfig chosenConfig;
  private final BuildingService buildingService;
  private ComponentMapper<Highlighted> highlightedMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;

  @Inject
  public BuildSystem(
      ChosenConfig chosenConfig,
      BuildingService buildingService
  ) {
    this.chosenConfig = chosenConfig;
    this.buildingService = buildingService;
  }

  @Override
  protected void process(int entityId) {
    if (chosenConfig.isAnyChosen() && chosenConfig.peekClass().equals(BuildingConfig.class)) {
      log.info("some are chosen and there's a sub highlighted entity");
      long buildingConfigId = chosenConfig.pop();
      var targetCoordinate = coordinatesMapper.get(entityId);
      buildingService.createBuilding(buildingConfigId, entityId, targetCoordinate);
      highlightedMapper.remove(entityId);
    }
  }
}
