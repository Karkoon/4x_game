package com.mygdx.game.client.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.assets.MenuScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class UiElementsCreator {

  private final Skin gameSkin;
  private final Skin menuSkin;

  @Inject
  public UiElementsCreator(
      GameScreenAssets gameAssets,
      MenuScreenAssets menuScreenAssets
  ) {
    gameSkin = gameAssets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    menuSkin = menuScreenAssets.getSkin(MenuScreenAssetPaths.SKIN);
  }

  public Button createActionButton(String text, Runnable runnable, int x, int y) {
    var button = new TextButton(text, gameSkin);
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
    var label = new Label(message, gameSkin);
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

  public Label createDialogLabel(String value) {
    var label = new Label(value, menuSkin);
    label.setAlignment(Align.center);
    return label;
  }

  public TextButton createDialogButton(String text) {
    return new TextButton(text, menuSkin);
  }

  public TextField createDialogTextField(String initialValue) {
    var textField = new TextField(initialValue, menuSkin);
    textField.setAlignment(Align.center);
    return textField;
  }

  public Dialog createDialog(String title) {
    var dialog = new Dialog(title, menuSkin);
    dialog.setMovable(false);
    dialog.setResizable(false);
    dialog.pad(20);
    dialog.getTitleLabel().setAlignment(Align.center);
    return dialog;
  }

  public Table createTable(float x, float y) {
    var table = new Table(menuSkin);
    table.setPosition(x, y);
    table.align(Align.top);
    return table;
  }

  public SelectBox createSelectBox() {
    var selectBox = new SelectBox(menuSkin);
    return selectBox;
  }

  public ScrollPane createScrollPane(Actor child) {
    var scrollPane = new ScrollPane(child, menuSkin);
    return scrollPane;
  }

  public void addCellToTable(Actor actor, Table table) {
    table.add(actor);
    table.row();
  }

  public void addToTableRow(Actor actor, Table table) {
    table.add(actor);
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

  public void setActorPosition(Actor actor, int x, int y) {
    actor.setPosition(x, y);
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

  public void changeColorOfActor(Actor actor, float r, float g, float b, float a) {
    actor.setColor(r, g, b, a);
  }
}
