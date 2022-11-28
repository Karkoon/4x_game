package com.mygdx.game.client.hud;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.model.ChosenConfig;
import com.mygdx.game.client.model.InField;
import com.mygdx.game.client.ui.CanNotCreateUnitFactory;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.service.CreateUnitService;
import com.mygdx.game.client_core.util.InfieldUtil;
import com.mygdx.game.client_core.util.MaterialUtilClient;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.InRecruitment;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
public class FieldScreenHUD implements Disposable {

  private final CanNotCreateUnitFactory canNotCreateUnitFactory;
  private final ChosenConfig chosenConfig;
  private final CreateUnitService createUnitService;
  private final GameConfigAssets gameConfigAssets;
  private final GameScreenAssets gameScreenAssets;
  private final InField inField;
  private final InfieldUtil infieldUtil;
  private final Lazy<MaterialUtilClient> materialUtilClient;
  private final PlayerInfo playerInfo;
  private final Stage stage;
  private final Texture inRecruitmentImageTexture;
  private final UiElementsCreator uiElementsCreator;

  private ComponentMapper<InRecruitment> inRecruitmentMapper;

  private VerticalGroup unitAndBuildingContainer;

  @Inject
  public FieldScreenHUD(
      CanNotCreateUnitFactory canNotCreateUnitFactory,
      CreateUnitService createUnitService,
      ChosenConfig chosenConfig,
      GameConfigAssets gameConfigAssets,
      GameScreenAssets gameScreenAssets,
      InField inField,
      InfieldUtil infieldUtil,
      Lazy<MaterialUtilClient> materialUtilClient,
      PlayerInfo playerInfo,
      @Named(StageModule.FIELD_SCREEN) Stage stage,
      UiElementsCreator uiElementsCreator,
      World world
  ) {
    this.canNotCreateUnitFactory = canNotCreateUnitFactory;
    this.chosenConfig = chosenConfig;
    this.createUnitService = createUnitService;
    this.gameConfigAssets = gameConfigAssets;
    this.gameScreenAssets = gameScreenAssets;
    this.inField = inField;
    this.infieldUtil = infieldUtil;
    this.inRecruitmentImageTexture = gameScreenAssets.getTexture("units/in_recruitment.png");
    this.materialUtilClient = materialUtilClient;
    this.playerInfo = playerInfo;
    this.stage = stage;
    this.uiElementsCreator = uiElementsCreator;

    world.inject(this);
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

  public void prepareHudSceleton() {
    log.info("Show infield");
    stage.clear();
    var container = uiElementsCreator.createVerticalContainer((int) stage.getWidth()/5*4, 0, (int) stage.getWidth()/5, (int) stage.getHeight());
    var buildingsButton = uiElementsCreator.createActionButton("Create building", this::createBuildingList, 0, 0);
    var unitButton = uiElementsCreator.createActionButton("Create unit", this::createUnitList, 0, 0);
    if (inField.getField() != -1 && inRecruitmentMapper.has(inField.getField())) {
      var inRecruitment = inRecruitmentMapper.get(inField.getField());
      var inRecruitmentImage = uiElementsCreator.createImage(inRecruitmentImageTexture, 0, 0);
      var inRecruitmentLabel = uiElementsCreator.createLabel("There is creating unit " + inRecruitment.getUnitConfigId() + ", turn left " + inRecruitment.getTurnLeft(), 10, 10);
      uiElementsCreator.addHoverPopupWithActor(inRecruitmentImage, inRecruitmentLabel, stage);
      stage.addActor(inRecruitmentImage);
    }
    container.addActor(buildingsButton);
    container.addActor(unitButton);
    stage.addActor(container);
  }

  private void createBuildingList() {
    if (unitAndBuildingContainer != null)
      unitAndBuildingContainer.remove();
    var buildings = gameConfigAssets.getGameConfigs().getAll(BuildingConfig.class);
    var sampleTexture = gameScreenAssets.getTexture(buildings.get(0).getIconName());
    this.unitAndBuildingContainer = uiElementsCreator.createVerticalContainer(100, 100, sampleTexture.getWidth(), sampleTexture.getHeight() * buildings.size);
    uiElementsCreator.removeActorAfterClick(unitAndBuildingContainer);
    for (int i = 0; i < buildings.size; i++) {
      var building = buildings.get(i);
      long buildingId = building.getId();

      var texture = gameScreenAssets.getTexture(building.getIconName());
      var imageButton = uiElementsCreator.createImageButton(texture, 0, i * texture.getHeight());
      imageButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          chooseBuilding(buildingId);
        }
      });
      unitAndBuildingContainer.addActor(imageButton);
    }

    stage.addActor(unitAndBuildingContainer);
  }

  private void createUnitList() {
    if (unitAndBuildingContainer != null)
      unitAndBuildingContainer.remove();
    var units = getUnits();
    var sampleTexture = gameScreenAssets.getTexture(units.get(0).getIconName());
    this.unitAndBuildingContainer = uiElementsCreator.createVerticalContainer(100, 100, sampleTexture.getWidth(), sampleTexture.getHeight() * units.size);
    uiElementsCreator.removeActorAfterClick(unitAndBuildingContainer);
    for (int i = 0; i < units.size; i++) {
      var unit = units.get(i);
      long unitId = unit.getId();

      var texture = gameScreenAssets.getTexture(unit.getIconName());
      var imageButton = uiElementsCreator.createImageButton(texture, 0, i * texture.getHeight());
      if (!infieldUtil.checkIfCanBuildUnit(inField.getField(), unitId)) {
        imageButton.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            unitAndBuildingContainer.remove();
            canNotCreateUnitFactory.createAndShow("You don't have enough buildings");
          }
        });
      } else if (inRecruitmentMapper.has(inField.getField())) {
        imageButton.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            unitAndBuildingContainer.remove();
            canNotCreateUnitFactory.createAndShow("There is another recruited unit");
          }
        });
      } else if (!infieldUtil.checkIfEnoughMaterialsToRecruitUnit(unitId)){
        imageButton.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            unitAndBuildingContainer.remove();
            canNotCreateUnitFactory.createAndShow("You don't have enough materials");
          }
        });
      } else {
        imageButton.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            choosenUnit(unitId);
          }
        });
      }

      unitAndBuildingContainer.addActor(imageButton);
    }

    stage.addActor(unitAndBuildingContainer);
  }

  private Array<UnitConfig> getUnits() {
    var allUnits = gameConfigAssets.getGameConfigs().getAll(UnitConfig.class);
    var civUnits = new Array<UnitConfig>();
    for (UnitConfig unit : allUnits) {
      if (unit.getCivilizationConfigId() == playerInfo.getCivilization())
        civUnits.add(unit);
    }
    return civUnits;
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
