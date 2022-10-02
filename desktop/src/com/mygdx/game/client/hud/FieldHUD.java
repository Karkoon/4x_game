package com.mygdx.game.client.hud;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.model.ChosenConfig;
import com.mygdx.game.client.model.InField;
import com.mygdx.game.client.util.HUDElementsCreator;
import com.mygdx.game.client_core.network.service.CreateUnitService;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
public class FieldHUD implements Disposable {

  private final Stage stage;
  private final HUDElementsCreator hudElementsCreator;
  private final ChosenConfig chosenConfig;
  private final GameConfigAssets assets;
  private final GameScreenAssets gameAssets;
  private final CreateUnitService createUnitService;
  private final InField inField;

  @Inject
  public FieldHUD(
      @NonNull @Named(StageModule.FIELD_SCREEN) Stage stage,
      @NonNull HUDElementsCreator hudElementsCreator,
      @NonNull ChosenConfig chosenConfig,
      @NonNull GameConfigAssets assets,
      @NonNull GameScreenAssets gameAssets,
      @NonNull CreateUnitService createUnitService,
      @NonNull InField inField
  ) {
    this.stage = stage;
    this.hudElementsCreator = hudElementsCreator;
    this.chosenConfig = chosenConfig;
    this.assets = assets;
    this.gameAssets = gameAssets;
    this.createUnitService = createUnitService;
    this.inField = inField;

    prepareHudSceleton();
  }

  public void act(float delta) {
    stage.act(delta);
  }

  public void draw() {
    stage.draw();
  }

  public void dispose (){
    stage.dispose();
  }

  private void prepareHudSceleton() {
    var container = hudElementsCreator.createVerticalContainer((int) stage.getWidth()/5*4, 0, (int) stage.getWidth()/5, (int) stage.getHeight());
    var buildingsButton = hudElementsCreator.createActionButton("Create building", this::createBuildingList);
    var unitButton = hudElementsCreator.createActionButton("Create unit", this::createUnitList);
    container.addActor(buildingsButton);
    container.addActor(unitButton);
    stage.addActor(container);
  }

  private void createBuildingList() {
    var buildings = assets.getGameConfigs().getAll(BuildingConfig.class);
    var texture = gameAssets.getTexture(buildings.get(0).getIconName());
    var container = hudElementsCreator.createVerticalContainer(100, 100, texture.getWidth(), texture.getHeight() * buildings.size);
    hudElementsCreator.removeAfterClick(container);
    for (int i = 0; i < buildings.size; i++) {
      var building = buildings.get(i);
      log.info("BUILDING: " + building.getName());
      long buildingId = building.getId();
      var imageButton = hudElementsCreator.createImageButton(building.getIconName(), 0, i * texture.getHeight());
      imageButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          chooseBuilding(buildingId);
        }
      });
      container.addActor(imageButton);
    }

    stage.addActor(container);
  }

  private void createUnitList() {
    var units = assets.getGameConfigs().getAll(UnitConfig.class);
    var texture = gameAssets.getTexture(units.get(0).getIconName());
    var container = hudElementsCreator.createVerticalContainer(100, 100, texture.getWidth(), texture.getHeight() * units.size);
    hudElementsCreator.removeAfterClick(container);
    for (int i = 0; i < units.size; i++) {
      var unit = units.get(i);
      log.info("UNIT: " + unit.getName());
      long unitId = unit.getId();
      var imageButton = hudElementsCreator.createImageButton(unit.getIconName(), 0, i * texture.getHeight());
      imageButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          choosenUnit(unitId);
        }
      });
      container.addActor(imageButton);
    }

    stage.addActor(container);
  }


  private void chooseBuilding(long buildingId) {
    log.info("Choosen building with id: " + buildingId);
    chosenConfig.addChosen(buildingId, BuildingConfig.class);
  }

  private void choosenUnit(long unitId) {
    log.info("Choosen unit with id: " + unitId);
    createUnitService.createUnit(unitId, inField.getField());
  }



}
