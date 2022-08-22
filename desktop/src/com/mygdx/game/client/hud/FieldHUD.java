package com.mygdx.game.client.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.util.HUDElementsCreator;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Named;

public class FieldHUD implements Disposable {

  private final Stage stage;
  private final HUDElementsCreator hudElementsCreator;

  @Inject
  public FieldHUD(
      @NonNull @Named(StageModule.FIELD_SCREEN) Stage stage,
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
    var table = hudElementsCreator.createHudSceleton(stage.getWidth(), stage.getHeight());
    var buildingsButton = hudElementsCreator.createActionButton("Create building", this::createTextField);
    table.add(buildingsButton);
    stage.addActor(table);
  }

  private void createTextField() {
    TextField hejka = hudElementsCreator.createTextField("HEJKA");
    stage.addActor(hejka);
  }


}
