package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.client.screen.GameScreen;
import com.mygdx.game.client.screen.LoadingScreen;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class MyGdxGame extends Game {

  private final Lazy<GameScreen> gameScreen;
  private final Lazy<LoadingScreen> loadingScreen;

  @Inject
  MyGdxGame(@NonNull Lazy<GameScreen> gameScreen,
            @NonNull Lazy<LoadingScreen> loadingScreen) {
    this.gameScreen = gameScreen;
    this.loadingScreen = loadingScreen;
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

  public void changeToGameScreen() {
    setScreen(gameScreen.get());
  }

  public void changeToLoadingScreen() {
    setScreen(loadingScreen.get());
  }
}
