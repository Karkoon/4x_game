package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.assets.MenuScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.ui.decorations.Planet;
import com.mygdx.game.client.ui.decorations.StarBackground;
import com.mygdx.game.core.util.Vector3Util;

import javax.inject.Inject;

public class MenuScreen extends ScreenAdapter {

  private static final Vector2 PLANET_SIZE = new Vector2(1000, 1000);
  private final GdxGame gdxGame;
  private final MenuScreenAssets menuScreenAssets;
  private Planet planet;
  private final Stage stage;
  private final StarBackground starBackground;

  @Inject
  public MenuScreen(
      GdxGame gdxGame,
      MenuScreenAssets menuScreenAssets,
      Stage stage,
      StarBackground starBackground
  ) {
    this.gdxGame = gdxGame;
    this.menuScreenAssets = menuScreenAssets;
    this.stage = stage;
    this.starBackground = starBackground;

    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void show() {
    stage.clear();
    prepareSceleton();
    Gdx.input.setInputProcessor(stage);
    planet = new Planet(menuScreenAssets, PLANET_SIZE, Vector3Util.toVector2(stage.getCamera().position));
  }

  private void prepareSceleton() {
    stage.clear();
    var mainMenu = createMenu();
    planet = new Planet(menuScreenAssets, PLANET_SIZE, Vector3Util.toVector2(stage.getCamera().position));
    stage.addActor(mainMenu);
  }

  @Override
  public void resize(int width, int height) {
    starBackground.resize(width, height);
    planet.resize(width, height);
    stage.getViewport().update(width, height, true);
    super.resize(width, height);
    prepareSceleton();
  }

  private Table createMenu() {
    var table = new Table();

    table.add().expandX().row();
    table.add(createStartButton()).fillX().expandY().fillY().row();
    table.add(createAboutButton()).fillX().expandY().fillY().row();
    table.add(createExitButton()).fillX().expandY().fillY().row();

    table.setSize((float) (stage.getWidth() * 0.2), (float) (stage.getHeight() * 0.2));
    table.setPosition((float) (stage.getWidth() * 0.4), (float) (stage.getHeight() * 0.4));
    return table;
  }

  private Button createStartButton() {
    return createFunctionalButton(gdxGame::changeToGameRoomListScreen, "START");
  }

  private Button createAboutButton() {
    return createFunctionalButton(gdxGame::changeToAboutScreen, "ABOUT");
  }

  private Button createExitButton() {
    return createFunctionalButton(gdxGame::exit, "EXIT");
  }

  private Button createFunctionalButton(final Runnable runnable, String text) {
    var button = new TextButton(text, menuScreenAssets.getSkin(MenuScreenAssetPaths.SKIN));
    button.getLabel().setFontScale(1.5f);
    button.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        runnable.run();
      }
    });
    return button;
  }

  @Override
  public void render(float delta) {
    planet.update(delta);
    starBackground.update(delta);

    stage.getBatch().begin();
    starBackground.draw(stage.getBatch(), stage.getCamera());
    planet.draw(stage.getBatch());
    stage.getBatch().end();

    stage.draw();
    stage.act(delta);
  }

}
