package com.mygdx.game.client.hud;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.model.ChosenConfig;
import com.mygdx.game.client.model.InField;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client.ui.CanNotCreateUnitFactory;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.service.CreateUnitService;
import com.mygdx.game.client_core.util.InfieldUtil;
import com.mygdx.game.client_core.util.MaterialUtilClient;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.InRecruitment;
import com.mygdx.game.core.model.BuildingImpactValue;
import com.mygdx.game.core.model.BuildingType;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

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
  private final Lazy<Navigator> navigator;

  private final PlayerInfo playerInfo;
  private final Stage stage;
  private final Texture inRecruitmentImageTexture;
  private final UiElementsCreator uiElementsCreator;

  private ComponentMapper<InRecruitment> inRecruitmentMapper;

  private Window unitAndBuildingContainer;

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
      Lazy<Navigator> navigator,
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
    this.navigator = navigator;
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

  public void resize() {
    prepareHudSceleton();
  }

  public void prepareHudSceleton() {
    log.info("Show infield");
    stage.clear();
    var buildingsButton = uiElementsCreator.createActionButton("Create building", this::createBuildingList, (int) (stage.getWidth() - 120), (int) (stage.getHeight() - 30));
    var unitButton = uiElementsCreator.createActionButton("Create unit", this::createUnitList, (int) (stage.getWidth() - 120), (int) (stage.getHeight() - 70));
    uiElementsCreator.setActorWidthAndHeight(buildingsButton, 120, 30);
    uiElementsCreator.setActorWidthAndHeight(unitButton, 120, 30);
    if (inField.getField() != -1 && inRecruitmentMapper.has(inField.getField())) {
      var inRecruitment = inRecruitmentMapper.get(inField.getField());
      var inRecruitmentImage = uiElementsCreator.createImage(inRecruitmentImageTexture, 0, 0);
      var inRecruitmentLabel = uiElementsCreator.createLabel("There is creating unit " + inRecruitment.getUnitConfigId() + ", turn left " + inRecruitment.getTurnLeft(), 10, 10);
      uiElementsCreator.addHoverPopupWithActor(inRecruitmentImage, inRecruitmentLabel, stage);
      stage.addActor(inRecruitmentImage);
    }
    stage.addActor(buildingsButton);
    stage.addActor(unitButton);

    var exitGameButton = uiElementsCreator.createActionButton("EXIT", this::exit, 0, (int) (stage.getHeight()-30));
    uiElementsCreator.setActorWidthAndHeight(exitGameButton, 100, 30);
    stage.addActor(exitGameButton);
  }

  private void createBuildingList() {
    if (unitAndBuildingContainer != null)
      unitAndBuildingContainer.remove();
    var buildings = gameConfigAssets.getGameConfigs().getAll(BuildingConfig.class);
    var sampleTexture = gameScreenAssets.getTexture(buildings.get(0).getIconName());
    var window = uiElementsCreator.createGameWindow("Building list");
    uiElementsCreator.setActorWidthAndHeight(window, sampleTexture.getWidth() + 50, sampleTexture.getHeight() * buildings.size + 30);
    var container =  uiElementsCreator.createVerticalContainer(100, 100, sampleTexture.getWidth(), sampleTexture.getHeight() * buildings.size);
    window.add(container);
    this.unitAndBuildingContainer = window;
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
      var buildingDescription = createBuildingDescription(building, 150, i * texture.getHeight() + 50);
      uiElementsCreator.addHoverPopupWithActor(imageButton, buildingDescription, stage);
      unitAndBuildingContainer.addActor(imageButton);
    }

    stage.addActor(unitAndBuildingContainer);
  }

  private Window createBuildingDescription(BuildingConfig building, int x, int y) {
    var description = uiElementsCreator.createVerticalContainer(x, y, 150, 350);
    var descriptionWindow = uiElementsCreator.createWindow("Building description");
    descriptionWindow.add(description);
    uiElementsCreator.setActorPosition(descriptionWindow, 150, 0);
    uiElementsCreator.setActorWidthAndHeight(descriptionWindow, 250, 400);

    var nameLabel = uiElementsCreator.createLabel(building.getName(), 0, 0);
    description.addActor(nameLabel);

    var turnLabel = uiElementsCreator.createLabel("Turn amount: " + building.getTurnAmount(), 0, 0);
    description.addActor(turnLabel);

    var materialsLabel = uiElementsCreator.createLabel("Materials", 0, 0);
    description.addActor(materialsLabel);

    for (Map.Entry<MaterialBase, MaterialUnit> materialEntry : building.getMaterials().entrySet()) {
      if (materialEntry.getValue().getAmount() > 0) {
        var material = uiElementsCreator.createHorizontalContainer(0, 0, 50, 40);
        var materialImage = uiElementsCreator.createImage(gameScreenAssets.getTexture(materialEntry.getKey().iconPath), 0, 0);
        var materialLabel = uiElementsCreator.createLabel(String.valueOf(materialEntry.getValue().getAmount()), 0, 0);
        material.addActor(materialImage);
        material.addActor(materialLabel);
        description.addActor(material);
      }
    }

    var impact = building.getImpact();
    var buildingType = uiElementsCreator.createLabel("Building type: " + impact.getBuildingType().name, 0, 0);
    description.addActor(buildingType);
    if (impact.getBuildingType() == BuildingType.MATERIALS_BUILDING) {
      var materialIncreaseLabel = uiElementsCreator.createLabel("Affected materials", 0, 0);
      description.addActor(materialIncreaseLabel);
      for (BuildingImpactValue buildingImpactValue : impact.getBuildingImpactValues()) {
        var materialBase = MaterialBase.valueOf(buildingImpactValue.getParameter().name());
        var material = uiElementsCreator.createHorizontalContainer(0, 0, 50, 40);
        var materialImage = uiElementsCreator.createImage(gameScreenAssets.getTexture(materialBase.iconPath), 0, 0);
        var materialLabel = uiElementsCreator.createLabel(buildingImpactValue.getOperation().name + " " + buildingImpactValue.getValue(), 0, 0);
        material.addActor(materialImage);
        material.addActor(materialLabel);
        description.addActor(material);
      }
    } else if (impact.getBuildingType() == BuildingType.RECRUITMENT_BUILDING) {
      var allowUnitsLabel = uiElementsCreator.createLabel("Can recruit", 0, 0);
      description.addActor(allowUnitsLabel);
      for (BuildingImpactValue buildingImpactValue : impact.getBuildingImpactValues()) {
        int unitConfigId = buildingImpactValue.getValue();
        var unitConfig = gameConfigAssets.getGameConfigs().get(UnitConfig.class, unitConfigId);
        var unitLabel = uiElementsCreator.createLabel(unitConfig.getName(), 0, 0);
        description.addActor(unitLabel);
      }
    }

    return descriptionWindow;
  }

  private void createUnitList() {
    if (unitAndBuildingContainer != null)
      unitAndBuildingContainer.remove();
    var units = getUnits();
    var sampleTexture = gameScreenAssets.getTexture(units.get(0).getIconName());
    var window = uiElementsCreator.createGameWindow("Unit list");
    uiElementsCreator.setActorWidthAndHeight(window, sampleTexture.getWidth() + 50, sampleTexture.getHeight() * units.size + 30);
    var container = uiElementsCreator.createVerticalContainer(100, 100, sampleTexture.getWidth(), sampleTexture.getHeight() * units.size);
    window.add(container);
    this.unitAndBuildingContainer = window;
    uiElementsCreator.removeActorAfterClick(unitAndBuildingContainer);
    for (int i = 0; i < units.size; i++) {
      var unit = units.get(i);
      long unitId = unit.getId();

      var texture = gameScreenAssets.getTexture(unit.getIconName());
      var imageButton = uiElementsCreator.createImageButton(texture, 0, i * texture.getHeight());
      var unitDescription = createUnitDescription(unit);
      uiElementsCreator.addHoverPopupWithActor(imageButton, unitDescription, stage);
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

  private Window createUnitDescription(UnitConfig unit) {
    var description = uiElementsCreator.createVerticalContainer(0, 0, 150, 350);
    var descriptionWindow = uiElementsCreator.createWindow("Unit description");
    descriptionWindow.add(description);
    uiElementsCreator.setActorPosition(descriptionWindow, 150, 0);
    uiElementsCreator.setActorWidthAndHeight(descriptionWindow, 250, 400);

    var nameLabel = uiElementsCreator.createLabel(unit.getName(), 0, 0);
    description.addActor(nameLabel);

    var turnLabel = uiElementsCreator.createLabel("Turn amount: " + unit.getTurnAmount(), 0, 0);
    description.addActor(turnLabel);

    var materialsLabel = uiElementsCreator.createLabel("Materials", 0, 0);
    description.addActor(materialsLabel);

    for (Map.Entry<MaterialBase, MaterialUnit> materialEntry : unit.getMaterials().entrySet()) {
      if (materialEntry.getValue().getAmount() > 0) {
        var material = uiElementsCreator.createHorizontalContainer(0, 0, 50, 40);
        var materialImage = uiElementsCreator.createImage(gameScreenAssets.getTexture(materialEntry.getKey().iconPath), 0, 0);
        var materialLabel = uiElementsCreator.createLabel(String.valueOf(materialEntry.getValue().getAmount()), 0, 0);
        material.addActor(materialImage);
        material.addActor(materialLabel);
        description.addActor(material);
      }
    }

    return descriptionWindow;
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

  private void exit() {
    navigator.get().changeToGameScreen();
  }
}
