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
  private final Stage stage;
  private final MenuScreenAssets assets;
  private final GdxGame navigator;
  private final StarBackground starBackground;
  private final Planet planet;

  @Inject
  public MenuScreen(
      Stage stage,
      MenuScreenAssets assets,
      GdxGame navigator
  ) {
    this.stage = stage;
    this.assets = assets;
    this.navigator = navigator;

    var mainMenu = createMenu();
    stage.addActor(mainMenu);
    starBackground = new StarBackground(assets, stage.getCamera());
    planet = new Planet(assets, PLANET_SIZE, Vector3Util.toVector2(stage.getCamera().position));

    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void resize(int width, int height) {
    starBackground.resize(width, height);
    planet.resize(width, height);
    stage.getViewport().update(width, height);
    super.resize(width, height);
  }

  private Table createMenu() {
    var table = new Table();

    table.add().expandX().row();
    table.add(createStartButton()).fillX().expandY().fillY().row();
    table.add(createAboutButton()).fillX().expandY().fillY().row();
    table.add(createExitButton()).fillX().expandY().fillY().row();

    var width = stage.getWidth() / 5;
    var height = stage.getHeight() / 5;

    table.setSize(width, height);
    table.setPosition((stage.getWidth() - width) / 2, (stage.getHeight() - height) / 2);
    return table;
  }

  private Button createStartButton() {
    return createFunctionalButton(navigator::changeToGameRoomListScreen, "START");
  }

  private Button createAboutButton() {
    return createFunctionalButton(navigator::changeToAboutScreen, "ABOUT");
  }

  private Button createExitButton() {
    return createFunctionalButton(navigator::exit, "EXIT");
  }

  private Button createFunctionalButton(final Runnable runnable, String text) {
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

  @Override
  public void render(float delta) {
    planet.update(delta);
    starBackground.update(delta);

    stage.getBatch().begin();
    starBackground.draw(stage.getBatch());
    planet.draw(stage.getBatch());
    stage.getBatch().setShader(null);
    stage.getBatch().end();

    stage.draw();
    stage.act(delta);
  }

}
