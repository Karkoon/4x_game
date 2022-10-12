package com.mygdx.game.client.hud;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.network.service.EndTurnService;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.model.MaterialBase;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
public class WorldHUD implements Disposable {

  private final EndTurnService endTurnService;
  private final GameScreenAssets gameAssets;
  private final Stage stage;
  private final UiElementsCreator uiElementsCreator;

  private Button endTurnButton;
  private HorizontalGroup materialGroup;

  @AspectDescriptor(one = {PlayerMaterial.class})
  private EntitySubscription subscription;
  private ComponentMapper<PlayerMaterial> playerMaterialMapper;

  @Inject
  public WorldHUD(
      GameScreenAssets gameScreenAssets,
      EndTurnService endTurnService,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      UiElementsCreator uiElementsCreator,
      World world
  ) {
    world.inject(this);

    this.endTurnService = endTurnService;
    this.gameAssets = gameScreenAssets;
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
    this.materialGroup = uiElementsCreator.createHorizontalContainer((int) (stage.getWidth()-300), (int) (stage.getHeight()-50), 300, 50);

    this.endTurnButton = uiElementsCreator.createActionButton("END TURN", this::addEndTurnAction, (int) (stage.getWidth()-150), 0);
    uiElementsCreator.setActorWidthAndHeight(this.endTurnButton, 150, 40);

    fillMaterialGroup();
    stage.addActor(materialGroup);
    stage.addActor(endTurnButton);
  }

  private void fillMaterialGroup() {
    if (subscription != null) {
      var allMaterials = MaterialBase.values();
      for (int i = 0; i < allMaterials.length; i++) {
        var material = allMaterials[i];
        int value = findConnectedValue(material);

        var texture = gameAssets.getTexture(material.iconPath);
        var image = uiElementsCreator.createImage(texture, i * 70, 0);
        var text = uiElementsCreator.createLabel(String.valueOf(value), i * 70 + 20, 0);
        this.materialGroup.addActor(image);
        this.materialGroup.addActor(text);
      }
    }
  }

  private int findConnectedValue(MaterialBase material) {
    for (int i = 0; i < subscription.getEntities().size(); i++) {
      int materialEntityId = subscription.getEntities().get(i);
      var materialComponent = playerMaterialMapper.get(materialEntityId);
      if (materialComponent.getMaterial() == material)
        return materialComponent.getValue();
    }
    return 0;
  }

  private void addEndTurnAction() {
    endTurnService.endTurn();
  }

}
