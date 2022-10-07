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
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.util.HUDElementsCreator;
import com.mygdx.game.client_core.network.service.EndTurnService;
import com.mygdx.game.core.ecs.component.MaterialComponent;
import com.mygdx.game.core.model.MaterialBase;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
public class WorldHUD implements Disposable {

  @AspectDescriptor(all = {MaterialComponent.class})
  private EntitySubscription subscription;

  private ComponentMapper<MaterialComponent> materialMapper;

  private final EndTurnService endTurnService;
  private final HUDElementsCreator hudElementsCreator;
  private final Stage stage;

  private HorizontalGroup materialGroup;
  private Button endTurnButton;

  @Inject
  public WorldHUD(
      World world,
      EndTurnService endTurnService,
      HUDElementsCreator hudElementsCreator,
      @Named(StageModule.GAME_SCREEN) Stage stage
  ) {
    world.inject(this);

    this.endTurnService = endTurnService;
    this.hudElementsCreator = hudElementsCreator;
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
    this.materialGroup = hudElementsCreator.createHorizontalContainer((int) (stage.getWidth()-200), (int) (stage.getHeight()-50), 200, 50);
    this.endTurnButton = hudElementsCreator.createButton("END TURN", (int) (stage.getWidth()-150), 0, 150, 40);

    fillMaterialGroup();
    addEndTurnAction();

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
        var image = hudElementsCreator.createImage(material.iconPath, i * 50, 0);
        var text = hudElementsCreator.createLabel(String.valueOf(value), i * 50 + 20, 0);
        this.materialGroup.addActor(image);
        this.materialGroup.addActor(text);
      }
    }
  }

  private int findConnectedValue(MaterialBase material) {
    for (int i = 0; i < subscription.getEntities().size(); i++) {
      int materialEntityId = subscription.getEntities().get(i);
      var materialComponent = materialMapper.get(materialEntityId);
      if (materialComponent.getMaterial() == material)
        return materialComponent.getValue();
    }
    return 0;
  }

  private void addEndTurnAction() {
    endTurnButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        endTurnService.endTurn();
      }
    });
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
