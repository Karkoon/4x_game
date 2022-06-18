package com.mygdx.game.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.client.bot.BotClient;
import com.mygdx.game.client.screen.GameScreen;
import com.mygdx.game.client.screen.LoadingScreen;
import com.mygdx.game.client.screen.MenuScreen;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class GdxGame extends Game {

  private final AssetManager assetManager;
  private final Lazy<GameScreen> gameScreen;
  private final Lazy<LoadingScreen> loadingScreen;
  private final Lazy<MenuScreen> menuScreen;

  @Inject
  public GdxGame(
      @NonNull AssetManager assetManager,
      @NonNull Lazy<GameScreen> gameScreen,
      @NonNull Lazy<LoadingScreen> loadingScreen,
      @NonNull Lazy<MenuScreen> menuScreen
  ) {
    this.assetManager = assetManager;
    this.gameScreen = gameScreen;
    this.loadingScreen = loadingScreen;
    this.menuScreen = menuScreen;
  }

  @Override
  public void create() {
    changeToLoadingScreen();
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

  public void changeToGameScreen() {
    setScreen(gameScreen.get());
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
}
