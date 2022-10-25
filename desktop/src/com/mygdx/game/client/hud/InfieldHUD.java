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
import com.mygdx.game.client.ui.CanNotCreateUnitFactory;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.network.service.CreateUnitService;
import com.mygdx.game.client_core.util.InfieldUtil;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.UnitConfig;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
public class InfieldHUD implements Disposable {

  private final CanNotCreateUnitFactory canNotCreateUnitFactory;
  private final ChosenConfig chosenConfig;
  private final CreateUnitService createUnitService;
  private final GameConfigAssets assets;
  private final GameScreenAssets gameAssets;
  private final InField inField;
  private final InfieldUtil infieldUtil;
  private final UiElementsCreator uiElementsCreator;
  private final Stage stage;

  @Inject
  public InfieldHUD(
      @Named(StageModule.FIELD_SCREEN) Stage stage,
      UiElementsCreator uiElementsCreator,
      CanNotCreateUnitFactory canNotCreateUnitFactory,
      ChosenConfig chosenConfig,
      InField inField,
      InfieldUtil infieldUtil,
      GameConfigAssets assets,
      GameScreenAssets gameAssets,
      CreateUnitService createUnitService
  ) {
    this.assets = assets;
    this.canNotCreateUnitFactory = canNotCreateUnitFactory;
    this.chosenConfig = chosenConfig;
    this.createUnitService = createUnitService;
    this.gameAssets = gameAssets;
    this.inField = inField;
    this.infieldUtil = infieldUtil;
    this.stage = stage;
    this.uiElementsCreator = uiElementsCreator;

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
    var container = uiElementsCreator.createVerticalContainer((int) stage.getWidth()/5*4, 0, (int) stage.getWidth()/5, (int) stage.getHeight());
    var buildingsButton = uiElementsCreator.createActionButton("Create building", this::createBuildingList, 0, 0);
    var unitButton = uiElementsCreator.createActionButton("Create unit", this::createUnitList, 0, 0);
    container.addActor(buildingsButton);
    container.addActor(unitButton);
    stage.addActor(container);
  }

  private void createBuildingList() {
    var buildings = assets.getGameConfigs().getAll(BuildingConfig.class);
    var sampleTexture = gameAssets.getTexture(buildings.get(0).getIconName());
    var container = uiElementsCreator.createVerticalContainer(100, 100, sampleTexture.getWidth(), sampleTexture.getHeight() * buildings.size);
    uiElementsCreator.removeActorAfterClick(container);
    for (int i = 0; i < buildings.size; i++) {
      var building = buildings.get(i);
      long buildingId = building.getId();

      var texture = gameAssets.getTexture(building.getIconName());
      var imageButton = uiElementsCreator.createImageButton(texture, 0, i * texture.getHeight());
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
    var sampleTexture = gameAssets.getTexture(units.get(0).getIconName());
    var container = uiElementsCreator.createVerticalContainer(100, 100, sampleTexture.getWidth(), sampleTexture.getHeight() * units.size);
    uiElementsCreator.removeActorAfterClick(container);
    for (int i = 0; i < units.size; i++) {
      var unit = units.get(i);
      long unitId = unit.getId();

      var texture = gameAssets.getTexture(unit.getIconName());
      var imageButton = uiElementsCreator.createImageButton(texture, 0, i * texture.getHeight());
      if (infieldUtil.checkIfCanBuildUnit(inField.getField(), unitId)) {
        imageButton.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            choosenUnit(unitId);
          }
        });
      } else {
        imageButton.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            container.remove();
            canNotCreateUnitFactory.createAndShow("You don't have enough buildings");
          }
        });
      }

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