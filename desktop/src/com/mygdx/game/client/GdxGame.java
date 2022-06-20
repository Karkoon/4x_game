package com.mygdx.game.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.client.di.component.DaggerGameComponent;
import com.mygdx.game.client.di.component.GameComponent;
import com.mygdx.game.client.di.module.NavigatorModule;
import com.mygdx.game.client.di.module.NetworkModule;
import lombok.extern.java.Log;

@Log
public class GdxGame extends Game implements Navigator {

  private static final String HOST = "127.0.0.1"; // todo create a dialog in menu that asks for ip etc
  private static final int PORT = 10666;
  private final GameComponent gameComponent;

  public GdxGame() {
    gameComponent = DaggerGameComponent.builder()
        .navigatorModule(new NavigatorModule(this))
        .build();
  }

  @Override
  public void create() {
    changeToLoadingScreen();
  }

  @Override
  public void changeToGameScreen() {
    var networkModule = new NetworkModule(HOST, PORT); // todo make it be passed in from menu screen, also room id
    var gameScreen = gameComponent.gameScreenComponentFactory().setNetworkModule(networkModule).gameScreen();
    setScreen(gameScreen);
  }

  @Override
  public void changeToAboutScreen() {
    /* intentionally left empty */
  }

  @Override
  public void changeToLoadingScreen() {
    setScreen(gameComponent.loadingScreenComponent().get());
  }

  @Override
  public void changeToMenuScreen() {
    setScreen(gameComponent.menuScreenComponent().get());
  }

  @Override
  public void exit() {
    dispose();
    Gdx.app.exit();
  }
}
