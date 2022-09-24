package com.mygdx.game.client.hud;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.util.HUDElementsCreator;
import com.mygdx.game.core.ecs.component.MaterialComponent;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Named;

public class WorldHUD implements Disposable {

  private final Stage stage;
  private final HUDElementsCreator hudElementsCreator;
  private ComponentMapper<MaterialComponent> materialMapper;

  @AspectDescriptor(all = {MaterialComponent.class})
  private EntitySubscription subscription;

  @Inject
  public WorldHUD(
      @NonNull @Named(StageModule.GAME_SCREEN) Stage stage,
      @NonNull HUDElementsCreator hudElementsCreator
  ) {
    this.stage = stage;
    this.hudElementsCreator = hudElementsCreator;

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
    var materialGroup = createMaterial();
    stage.addActor(materialGroup);
  }

  private HorizontalGroup createMaterial() {
    var materialGroup = hudElementsCreator.createHorizontalContainer((int) (stage.getWidth()-150), 0, 150, 50);

    if (subscription != null) {
      for (int i = 0; i < subscription.getEntities().size(); i++) {
        int entityId = subscription.getEntities().get(i);
        var materialComponent = materialMapper.get(entityId);
        var image = hudElementsCreator.createImage(materialComponent.getMaterial().iconPath, i * 50, 0);
        var text = hudElementsCreator.createTextField(String.valueOf(materialComponent.getValue()));
        materialGroup.addActor(image);
        materialGroup.addActor(text);
      }
    }
    return materialGroup;
  }

}
