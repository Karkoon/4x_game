package com.mygdx.game.client.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.client.ecs.component.TextureComp;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UiElementsCreator {

  private final MenuScreenAssets assets;

  @Inject
  public UiElementsCreator(
      @NonNull MenuScreenAssets assets
  ) {
    this.assets = assets;
  }

  public Image createImage(Vector3 position, TextureComp texture) {
    var image = new Image(new TextureRegionDrawable(new TextureRegion(texture.getTexture())));
    image.setPosition(position.x, position.z);
    return image;
  }

  public TextField createTextField(Vector3 position, String message) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    skin.getFont("default-font").getData().scale(1.5f);

    TextField textField = new TextField(message, skin);
    textField.setPosition(position.x + 200, position.z + 200);
    textField.setHeight(200);
    textField.setWidth(600);

    return textField;
  }

  public void addHoverPopupWithActor(Actor uiElement, Actor popup, Stage stage) {
    uiElement.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        stage.addActor(popup);
      }
    });

    uiElement.addListener(new ClickListener() {
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        popup.remove();
      }
    });
  }
}
