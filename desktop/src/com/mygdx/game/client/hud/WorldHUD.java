package com.mygdx.game.client.hud;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.util.HUDElementsCreator;
import com.mygdx.game.core.ecs.component.MaterialComponent;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
public class WorldHUD implements Disposable {

  private final Stage stage;
  private final HUDElementsCreator hudElementsCreator;
  private ComponentMapper<MaterialComponent> materialMapper;

  @AspectDescriptor(all = {MaterialComponent.class})
  private EntitySubscription subscription;

  private HorizontalGroup materialGroup;

  @Inject
  public WorldHUD(
      World world,
      @NonNull @Named(StageModule.GAME_SCREEN) Stage stage,
      @NonNull HUDElementsCreator hudElementsCreator
  ) {
    world.inject(this);
    this.stage = stage;
    this.hudElementsCreator = hudElementsCreator;

    prepareHudSceleton();
  }

  public void act(float delta) {
    stage.act(delta);
  }

  public void draw() {
    fillMaterialGroup();
    stage.draw();
  }

  public void dispose (){
    stage.dispose();
  }

  private void prepareHudSceleton() {
    this.materialGroup = hudElementsCreator.createHorizontalContainer((int) (stage.getWidth()-150), 0, 150, 50);
    fillMaterialGroup();
    stage.addActor(materialGroup);
  }

  public void fillMaterialGroup() {
    log.info("TRY TO FILL1");
    this.materialGroup.clear();
    if (subscription != null) {
      subscription.getEntities();
      for (int i = 0; i < subscription.getEntities().size(); i++) {
        log.info("TRY TO FILL2");
        int entityId = subscription.getEntities().get(i);
        var materialComponent = materialMapper.get(entityId);
        var image = hudElementsCreator.createImage(materialComponent.getMaterial().iconPath, i * 50, 0);
        var text = hudElementsCreator.createTextField(String.valueOf(materialComponent.getValue()));
        this.materialGroup.addActor(image);
        this.materialGroup.addActor(text);
      }
    }
  }

}
