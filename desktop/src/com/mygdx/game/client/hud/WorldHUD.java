package com.mygdx.game.client.hud;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.model.PredictedIncome;
import com.mygdx.game.client_core.network.service.EndTurnService;
import com.mygdx.game.client_core.util.MaterialUtilClient;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.model.MaterialBase;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Log
public class WorldHUD implements Disposable {

  private final EndTurnService endTurnService;
  private final GameScreenAssets gameAssets;
  private final MaterialUtilClient materialUtilClient;
  private final PredictedIncome predictedIncome;
  private final Stage stage;
  private final UiElementsCreator uiElementsCreator;

  private Button endTurnButton;
  private HorizontalGroup materialGroup;

  private ComponentMapper<PlayerMaterial> playerMaterialMapper;

  @Inject
  public WorldHUD(
      EndTurnService endTurnService,
      GameScreenAssets gameScreenAssets,
      MaterialUtilClient materialUtilClient,
      PredictedIncome predictedIncome,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      UiElementsCreator uiElementsCreator,
      World world
  ) {
    world.inject(this);

    this.endTurnService = endTurnService;
    this.gameAssets = gameScreenAssets;
    this.materialUtilClient = materialUtilClient;
    this.predictedIncome = predictedIncome;
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

  public void prepareHudSceleton() {
    stage.clear();

    this.endTurnButton = uiElementsCreator.createActionButton("END TURN", this::addEndTurnAction, (int) (stage.getWidth()-150), 0);
    uiElementsCreator.setActorWidthAndHeight(this.endTurnButton, 150, 40);

    this.materialGroup = uiElementsCreator.createHorizontalContainer((int) (stage.getWidth()-300), (int) (stage.getHeight()-50), 300, 50);
    var popupMaterial = uiElementsCreator.createHorizontalContainer((int) (stage.getWidth()-300), (int) (stage.getHeight()-100), 300, 50);
    fillMaterialGroup(this.materialGroup, materialUtilClient.getPlayerMaterial());
    fillMaterialGroup(popupMaterial, predictedIncome.getIncomes());
    uiElementsCreator.addHoverPopupWithActor(this.materialGroup, popupMaterial, stage);


    stage.addActor(materialGroup);
    stage.addActor(endTurnButton);
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

  private void addEndTurnAction() {
    endTurnService.endTurn();
  }

}
