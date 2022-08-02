package com.mygdx.game.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.client.screen.FieldScreen;
import com.mygdx.game.client.screen.GameScreen;
import com.mygdx.game.client.screen.LoadingScreen;
import com.mygdx.game.client.screen.MenuScreen;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client.screen.TechnologyScreen;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class GdxGame extends Game implements Navigator {

  private final AssetManager assetManager;
  private final Lazy<GameScreen> gameScreen;
  private final Lazy<FieldScreen> fieldScreen;
  private final Lazy<LoadingScreen> loadingScreen;
  private final Lazy<MenuScreen> menuScreen;
  private final Lazy<TechnologyScreen> technologyScreen;

  @Inject
  GdxGame(
      @NonNull AssetManager assetManager,
      @NonNull Lazy<GameScreen> gameScreen,
      @NonNull Lazy<FieldScreen> fieldScreen,
      @NonNull Lazy<LoadingScreen> loadingScreen,
      @NonNull Lazy<MenuScreen> menuScreen,
      @NonNull Lazy<TechnologyScreen> technologyScreen
  ) {
    this.assetManager = assetManager;
    this.gameScreen = gameScreen;
    this.fieldScreen = fieldScreen;
    this.loadingScreen = loadingScreen;
    this.menuScreen = menuScreen;
    this.technologyScreen = technologyScreen;
  }

  @Override
  public void create() {
    changeTo(Direction.LOADING_SCREEN);
  }

  @Override
  public void render() {
    ScreenUtils.clear(0f, 0f, 0f, 1, true);
    super.render();
  }

  @Override
  public void dispose() {
    super.dispose();
    assetManager.dispose();
    Gdx.app.exit();
  }

  public void changeTo(Direction screenDirection) {
    switch (screenDirection) {
      case GAME_SCREEN -> changeToGameScreen();
      case FIELD_SCREEN -> changeToFieldScreen();
      case TECHNOLOGY_SCREEN -> changeToTechnologyScreen();
      case LOADING_SCREEN -> changeToLoadingScreen();
      case MENU_SCREEN -> changeToMenuScreen();
      case ABOUT_SCREEN -> changeToAboutScreen();
      case EXIT -> exit();
    }
  }

  public void changeToGameScreen() {
    setScreen(gameScreen.get());
  }

  public void changeToFieldScreen() {
    setScreen(fieldScreen.get());
  }

  public void changeToLoadingScreen() {
    setScreen(loadingScreen.get());
  }

  public void changeToMenuScreen() {
    setScreen(menuScreen.get());
  }

  public void changeToAboutScreen() {
    /* intentionally left empty */
  }

  public void changeToTechnologyScreen() {
    setScreen(technologyScreen.get());
  }

  @Override
  public void exit() {
    dispose();
  }

}
