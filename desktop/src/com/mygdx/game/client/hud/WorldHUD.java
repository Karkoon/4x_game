package com.mygdx.game.client.hud;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.network.service.EndTurnService;
import com.mygdx.game.core.ecs.component.MaterialComponent;
import com.mygdx.game.client.util.HUDElementsCreator;
import com.mygdx.game.core.ecs.component.PlayerMaterial;
import com.mygdx.game.core.model.MaterialBase;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
public class WorldHUD implements Disposable {

  private final GameScreenAssets gameAssets;
  private final EndTurnService endTurnService;
  private final UiElementsCreator uiElementsCreator;
  private final Stage stage;
  private final HUDElementsCreator hudElementsCreator;
  private ComponentMapper<PlayerMaterial> playerMaterialMapper;

  @AspectDescriptor(all = {PlayerMaterial.class})
  private Button endTurnButton;
  private HorizontalGroup materialGroup;

  @AspectDescriptor(all = {PlayerMaterialComponent.class})
  private EntitySubscription subscription;
  private ComponentMapper<MaterialComponent> materialMapper;

  @Inject
  public WorldHUD(
      World world,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      HUDElementsCreator hudElementsCreator
      GameScreenAssets gameScreenAssets,
      EndTurnService endTurnService,
      UiElementsCreator uiElementsCreator,
      @Named(StageModule.GAME_SCREEN) Stage stage
  ) {
    world.inject(this);

    this.gameAssets = gameScreenAssets;
    this.endTurnService = endTurnService;
    this.uiElementsCreator = uiElementsCreator;
    this.stage = stage;

    prepareHudSceleton();
    addListeners();
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
    this.materialGroup = uiElementsCreator.createHorizontalContainer((int) (stage.getWidth()-200), (int) (stage.getHeight()-50), 200, 50);

    this.endTurnButton = uiElementsCreator.createActionButton("END TURN", this::addEndTurnAction, (int) (stage.getWidth()-150), 0);
    uiElementsCreator.setActorWidthAndHeight(this.endTurnButton, 150, 40);

    fillMaterialGroup();

    stage.addActor(materialGroup);
    stage.addActor(endTurnButton);
  }

  private void fillMaterialGroup() {
    this.materialGroup.clearChildren();
    if (subscription != null) {
      var allMaterials = MaterialBase.values();
      for (int i = 0; i < allMaterials.length; i++) {
        var material = allMaterials[i];
        int value = findConnectedValue(material);

        var texture = gameAssets.getTexture(material.iconPath);
        var image = uiElementsCreator.createImage(texture, i * 50, 0);
        var text = uiElementsCreator.createLabel(String.valueOf(value), i * 50 + 20, 0);
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

  private void addListeners() {
    subscription.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
      @Override
      public void inserted(IntBag entities) {
        fillMaterialGroup();
      }

      @Override
      public void removed(IntBag entities) {
      }
    });
  }

}
