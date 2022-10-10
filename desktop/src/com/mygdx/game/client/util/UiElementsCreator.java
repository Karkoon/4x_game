package com.mygdx.game.client.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class UiElementsCreator {

  private final GameScreenAssets gameAssets;
  private final Skin skin;

  @Inject
  public UiElementsCreator(
      GameScreenAssets gameAssets
  ) {
    this.gameAssets = gameAssets;
    skin = gameAssets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
  }

  public Button createActionButton(String text, Runnable runnable, int x, int y) {
    var button = new TextButton(text, skin);
    button.setPosition(x, y);
    button.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        runnable.run();
      }
    });
    return button;
  }

  public Image createImage(Texture texture, int x, int y) {
    var image = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
    image.setPosition(x, y);
    return image;
  }

  public ImageButton createImageButton(Texture texture, int x, int y) {
    var imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(texture)));
    imageButton.setPosition(x, y);

    return imageButton;
  }

  public Label createLabel(String message, int x, int y) {
    var label = new Label(message, skin);
    label.setPosition(x, y);

    return label;
  }

  public VerticalGroup createVerticalContainer(int x, int y, int width, int height) {
    var verticalGroup = new VerticalGroup();
    verticalGroup.setPosition(x, y);
    verticalGroup.setWidth(width);
    verticalGroup.setHeight(height);

    return verticalGroup;
  }

  public HorizontalGroup createHorizontalContainer(int x, int y, int width, int height) {
    var horizontalGroup = new HorizontalGroup();
    horizontalGroup.setPosition(x, y);
    horizontalGroup.setWidth(width);
    horizontalGroup.setHeight(height);

    return horizontalGroup;
  }

  public void scaleLabel(Label label, float scale) {
    label.setFontScale(scale);
    label.setWidth(label.getWidth() * scale);
    label.setHeight(label.getHeight() * scale);
  }

  public void setActorWidthAndHeight(Actor actor, int width, int height) {
    actor.setWidth(width);
    actor.setHeight(height);
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

  public void removeActorAfterClick(Actor actor) {
    actor.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        actor.remove();
      }
    });
  }
}
