package com.mygdx.game.client.hud;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client.ui.NoUnitWithMoveRangeFactory;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.model.PredictedIncome;
import com.mygdx.game.client_core.network.service.EndTurnService;
import com.mygdx.game.client_core.util.MaterialUtilClient;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.model.MaterialBase;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Log
public class GameScreenHUD implements Disposable {

  private final ChosenEntity chosenEntity;
  private final EndTurnService endTurnService;
  private final GameScreenAssets gameAssets;
  private final MaterialUtilClient materialUtilClient;
  private final Lazy<Navigator> navigator;
  private final NoUnitWithMoveRangeFactory noUnitWithMoveRangeFactory;
  private final PlayerInfo playerInfo;
  private final PredictedIncome predictedIncome;
  private final Stage stage;
  private final UiElementsCreator uiElementsCreator;
  private final Viewport viewport;

  private Button endTurnButton;
  private Button nextUnitButton;
  private Button techScreenButton;
  private Button exitGameButton;
  private HorizontalGroup materialGroup;
  private Window activeUnitDescription;

  @AspectDescriptor(one = {Movable.class, Owner.class, Stats.class})
  private EntitySubscription playerUnitsSubscriptions;

  private ComponentMapper<CanAttack> canAttackMapper;
  private ComponentMapper<Movable> movableMapper;
  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<Stats> statsMapper;

  @Inject
  public GameScreenHUD(
      ChosenEntity chosenEntity,
      EndTurnService endTurnService,
      GameScreenAssets gameScreenAssets,
      MaterialUtilClient materialUtilClient,
      NoUnitWithMoveRangeFactory noUnitWithMoveRangeFactory,
      Lazy<Navigator> navigator,
      PlayerInfo playerInfo,
      PredictedIncome predictedIncome,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      UiElementsCreator uiElementsCreator,
      Viewport viewport,
      World world
  ) {
    world.inject(this);

    this.chosenEntity = chosenEntity;
    this.endTurnService = endTurnService;
    this.gameAssets = gameScreenAssets;
    this.materialUtilClient = materialUtilClient;
    this.navigator = navigator;
    this.noUnitWithMoveRangeFactory = noUnitWithMoveRangeFactory;
    this.playerInfo = playerInfo;
    this.predictedIncome = predictedIncome;
    this.stage = stage;
    this.uiElementsCreator = uiElementsCreator;
    this.viewport = viewport;
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
    stage.clear();

    this.endTurnButton = uiElementsCreator.createActionButton("END TURN", this::endTurn, (int) (stage.getWidth()-100), (int) (stage.getHeight()-30));
    uiElementsCreator.setActorWidthAndHeight(this.endTurnButton, 100, 30);

    this.nextUnitButton = uiElementsCreator.createActionButton("NEXT UNIT", this::selectNextUnit,  (int) (stage.getWidth()-210), (int) (stage.getHeight()-30));
    uiElementsCreator.setActorWidthAndHeight(this.nextUnitButton, 100, 30);

    this.techScreenButton = uiElementsCreator.createActionButton("TECH SCREEN", this::changeToTechnologyScreen,  (int) (stage.getWidth()-320), (int) (stage.getHeight()-30));
    uiElementsCreator.setActorWidthAndHeight(this.techScreenButton, 100, 30);

    this.exitGameButton = uiElementsCreator.createActionButton("EXIT", this::exit, 0, (int) (stage.getHeight()-30));
    uiElementsCreator.setActorWidthAndHeight(this.exitGameButton, 100, 30);

    this.materialGroup = uiElementsCreator.createHorizontalContainer(0, 0, 300, 50);
    var popupMaterial = uiElementsCreator.createHorizontalContainer(0, 60, 300, 50);
    fillMaterialGroup(materialGroup, materialUtilClient.getPlayerMaterial());
    fillMaterialGroup(popupMaterial, predictedIncome.getIncomes());
    uiElementsCreator.addHoverPopupWithActor(this.materialGroup, popupMaterial, stage);

    if (chosenEntity.isAnyChosen() && movableMapper.has(chosenEntity.peek())) {
      this.activeUnitDescription = prepareUnitGroup();
      stage.addActor(activeUnitDescription);
    }

    stage.addActor(materialGroup);
    stage.addActor(endTurnButton);
    stage.addActor(nextUnitButton);
    stage.addActor(techScreenButton);
    stage.addActor(exitGameButton);
  }

  private void fillMaterialGroup(HorizontalGroup group, Map<MaterialBase, Integer> playerMaterial) {
    var allMaterials = MaterialBase.values();
    for (int i = 0; i < allMaterials.length; i++) {
      var material = allMaterials[i];
      int value = 0;
      if (playerMaterial.containsKey(material))
        value = playerMaterial.get(material);

      var texture = gameAssets.getTexture(material.iconPath);
      var image = uiElementsCreator.createImage(texture, i * 70, 0);
      var text = uiElementsCreator.createLabel(String.valueOf(value), i * 70 + 20, 0);
      group.addActor(image);
      group.addActor(text);
    }
  }

  private Window prepareUnitGroup() {
    var activeUnitGroup = uiElementsCreator.createVerticalContainer(0, (int) (stage.getHeight() - 200), 170, 200);

    var name = nameMapper.get(chosenEntity.peek());
    var activeUnitDescriptionWindow = uiElementsCreator.createWindow(name.getName());

    uiElementsCreator.setActorPosition(activeUnitDescriptionWindow, (int) (stage.getWidth() * 0.05), (int) (stage.getHeight() - 250));
    uiElementsCreator.setActorWidthAndHeight(activeUnitDescriptionWindow, 170, 200);

    activeUnitDescriptionWindow.add(activeUnitGroup);

    var stats = statsMapper.get(chosenEntity.peek());
    var hpLabel = uiElementsCreator.createLabel("hp: " + stats.getHp() + "/" + stats.getMaxHp(), 0, 0);
    activeUnitGroup.addActor(hpLabel);
    var attackLabel = uiElementsCreator.createLabel("attack: " + stats.getAttackPower() + " range: " + stats.getAttackRange(), 0, 0);
    activeUnitGroup.addActor(attackLabel);
    var defenseLabel = uiElementsCreator.createLabel("defense: " + stats.getDefense(), 0, 0);
    activeUnitGroup.addActor(defenseLabel);
    var moveLabel = uiElementsCreator.createLabel("moveRange: " + stats.getMoveRange() + "/" + stats.getMaxMoveRange(), 0, 0);
    activeUnitGroup.addActor(moveLabel);
    var sightLabel = uiElementsCreator.createLabel("sight: " + stats.getSightRadius(), 0, 0);
    activeUnitGroup.addActor(sightLabel);

    return activeUnitDescriptionWindow;
  }

  private void endTurn() {
    endTurnService.endTurn();
  }

  public void selectNextUnit() {
    // Check if unit can move
    for (int i = 0; i < playerUnitsSubscriptions.getEntities().size(); i++) {
      int entityId = playerUnitsSubscriptions.getEntities().get(i);
      var owner = ownerMapper.get(entityId);
      var stats = statsMapper.get(entityId);
      if (playerInfo.getToken().equals(owner.getToken()) && stats != null && stats.getMoveRange() > 0) {
        positionCamera(entityId);
        return;
      }
    }
    // Check if unit can attack
    for (int i = 0; i < playerUnitsSubscriptions.getEntities().size(); i++) {
      int entityId = playerUnitsSubscriptions.getEntities().get(i);
      var owner = ownerMapper.get(entityId);
      var canAttack = canAttackMapper.get(entityId);
      if (playerInfo.getToken().equals(owner.getToken()) && canAttack != null && canAttack.isCanAttack()) {
        positionCamera(entityId);
        return;
      }
    }
    noUnitWithMoveRangeFactory.createAndShow("There is no available unit with move range");
  }

  private void changeToTechnologyScreen() {
    navigator.get().changeToTechnologyScreen();
  }

  private void exit() {
    navigator.get().exit();
  }

  private void positionCamera(int entityId) {
    var positionValue = positionMapper.get(entityId).getValue();
    var cameraHeight = viewport.getCamera().position.y;
    viewport.getCamera().position.set(positionValue.x, cameraHeight, positionValue.z);
    viewport.getCamera().lookAt(positionValue.x, 0, positionValue.z);
    chosenEntity.addChosen(entityId);
    prepareHudSceleton();
  }

}
