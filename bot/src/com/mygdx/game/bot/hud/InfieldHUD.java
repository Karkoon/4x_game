package com.mygdx.game.bot.hud;

import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client_core.network.service.CreateUnitService;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.UnitConfig;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class InfieldHUD {

  private final CreateUnitService createUnitService;
  private final GameConfigAssets assets;

  @Inject
  public InfieldHUD(
      GameConfigAssets assets,
      CreateUnitService createUnitService,
      World world
  ) {
    this.assets = assets;
    this.createUnitService = createUnitService;
    world.inject(this);
    prepareHudSceleton();
  }

  public void prepareHudSceleton() {
    log.info("Show infield");
    // todo track that a building is already being built
    // todo track that a unit is already being built
    // todo create a list of recrutable units and buildings
  }

  private void createBuildingList() {
    var buildings = assets.getGameConfigs().getAll(BuildingConfig.class);
    for (int i = 0; i < buildings.size; i++) {
      var building = buildings.get(i);
      var buildingId = building.getId();
    }
  }

  private void createUnitList() {
    var units = assets.getGameConfigs().getAll(UnitConfig.class);
    for (int i = 0; i < units.size; i++) {
      var unit = units.get(i);
      var unitId = unit.getId();
    }
  }


  private void chooseBuilding(long buildingId) {
    log.info("Choosen building with id: " + buildingId);
    // todo choose random building

    //chosenConfig.addChosen(buildingId, BuildingConfig.class); // create building service?
  }

  private void choosenUnit(long unitId) {
    log.info("Choosen unit with id: " + unitId);
    // todo choose random unit id
    //createUnitService.createUnit(unitId, inField.getField());
  }



}
