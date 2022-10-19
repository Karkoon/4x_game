package com.mygdx.game.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.client.screen.GameRoomListScreen;
import com.mygdx.game.client.screen.GameRoomScreen;
import com.mygdx.game.client.screen.LoadingScreen;
import com.mygdx.game.client.screen.MenuScreen;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class GdxGame extends Game {

  private final AssetManager assetManager;
  private final Lazy<LoadingScreen> loadingScreen;
  private final Lazy<MenuScreen> menuScreen;
  private final Lazy<GameRoomListScreen> gameRoomListScreen;
  private final Lazy<GameRoomScreen> gameRoomScreen;

  @Inject
  GdxGame(
      AssetManager assetManager,
      Lazy<LoadingScreen> loadingScreen,
      Lazy<MenuScreen> menuScreen,
      Lazy<GameRoomListScreen> gameRoomListScreen,
      Lazy<GameRoomScreen> gameRoomScreen
  ) {
    this.assetManager = assetManager;
    this.loadingScreen = loadingScreen;
    this.menuScreen = menuScreen;
    this.gameRoomListScreen = gameRoomListScreen;
    this.gameRoomScreen = gameRoomScreen;
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

  public void changeToGameRoomListScreen() {
    setScreen(gameRoomListScreen.get());
  }

  public void changeToGameRoomScreen() {
    setScreen(gameRoomScreen.get());
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

  public void exit() {
    dispose();
  }

}
