package com.mygdx.game.client.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.assets.MenuScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class HUDElementsCreator {

  private final MenuScreenAssets assets;

  @Inject
  public HUDElementsCreator(
      @NonNull MenuScreenAssets assets
  ) {
    this.assets = assets;
  private final GameScreenAssets gameAssets;

  @Inject
  public HUDElementsCreator(
      @NonNull MenuScreenAssets assets,
      @NonNull GameScreenAssets gameAssets
  ) {
    this.assets = assets;
    this.gameAssets = gameAssets;
  }

  public Table createHudSceleton(float width, float height) {
    var table = new Table();
    table.setWidth(width/5);
    table.setHeight(height);
    table.setPosition(width*4/5, 0);
    return table;
  }

  public Button createActionButton(String text, Runnable runnable) {
    var button = new TextButton(text, assets.getSkin(MenuScreenAssetPaths.SKIN));
    button.getLabel().setFontScale(1.5f);
    button.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        runnable.run();
      }
    });
    return button;
  }

  public TextField createTextField(String message, int x, int y) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);

    var textField = new TextField(message, skin);
    textField.setPosition(x, y);
    textField.setAlignment(25);
    textField.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        textField.remove();
      }
    });

    return textField;
  }

  public Label createLabel(String message, int x, int y) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);

    var label = new Label(message, skin);
    label.setPosition(x, y);
    label.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        label.remove();
      }
    });

    return label;
  }

  public VerticalGroup createVerticalContainer(int x, int y, int width, int height) {
    var verticalGroup = new VerticalGroup();
    verticalGroup.setPosition(x, y);
    verticalGroup.setWidth(width);
    verticalGroup.setHeight(height);

    verticalGroup.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        verticalGroup.remove();
      }
    });

    return verticalGroup;
  }

  public HorizontalGroup createHorizontalContainer(int x, int y, int width, int height) {
    var horizontalGroup = new HorizontalGroup();
    horizontalGroup.setPosition(x, y);
    horizontalGroup.setWidth(width);
    horizontalGroup.setHeight(height);

    return horizontalGroup;
  }

  public ImageButton createImageButton(String textureName, int x, int y) {
    var texture = gameAssets.getTexture(textureName);
    var imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(texture)));
    imageButton.setPosition(x, y);

    return imageButton;
  }

  public Image createImage(String textureName, int x, int y) {
    var texture = gameAssets.getTexture(textureName);
    var image = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
    image.setPosition(x, y);

    return image;
  }
}