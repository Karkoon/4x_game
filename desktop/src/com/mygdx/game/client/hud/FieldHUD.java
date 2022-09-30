package com.mygdx.game.client.hud;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.model.ChosenConfig;
import com.mygdx.game.client.util.HUDElementsCreator;
import com.mygdx.game.config.BuildingConfig;
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

  @Inject
  public FieldHUD(
      @NonNull @Named(StageModule.FIELD_SCREEN) Stage stage,
      @NonNull HUDElementsCreator hudElementsCreator,
      @NonNull ChosenConfig chosenConfig,
      @NonNull GameConfigAssets assets,
      @NonNull GameScreenAssets gameAssets
  ) {
    this.stage = stage;
    this.hudElementsCreator = hudElementsCreator;
    this.chosenConfig = chosenConfig;
    this.assets = assets;
    this.gameAssets = gameAssets;

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
    var table = hudElementsCreator.createHudSceleton(stage.getWidth(), stage.getHeight());
    var buildingsButton = hudElementsCreator.createActionButton("Create building", this::createBuildingList);
    table.add(buildingsButton);
    stage.addActor(table);
  }

  private void createBuildingList() {
    var buildings = assets.getGameConfigs().getAll(BuildingConfig.class);
    var texture = gameAssets.getTexture(buildings.get(0).getIconName());
    var container = hudElementsCreator.createVerticalContainer(100, 100, texture.getWidth(), texture.getHeight() * buildings.size);

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

  private void chooseBuilding(long buildingId) {
    log.info("Choosen building with id: " + buildingId);
    chosenConfig.addChosen(buildingId, BuildingConfig.class);
  }


}
