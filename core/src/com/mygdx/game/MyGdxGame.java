package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketAdapter;
import com.github.czyzby.websocket.WebSockets;
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
public class MyGdxGame extends Game {

  private final AssetManager assetManager;
  private final Lazy<GameScreen> gameScreen;
  private final Lazy<LoadingScreen> loadingScreen;
  private final Lazy<MenuScreen> menuScreen;

  @Inject
  MyGdxGame(@NonNull AssetManager assetManager,
            @NonNull Lazy<GameScreen> gameScreen,
            @NonNull Lazy<LoadingScreen> loadingScreen,
            @NonNull Lazy<MenuScreen> menuScreen) {
    this.assetManager = assetManager;
    this.gameScreen = gameScreen;
    this.loadingScreen = loadingScreen;
    this.menuScreen = menuScreen;
  }

  @Override
  public void create() {
    CommonWebSockets.initiate();
    // Note: you can also use WebSockets.newSocket() and WebSocket.toWebSocketUrl() methods.
    WebSocket socket = WebSockets.newSocket(WebSockets.toWebSocketUrl("localhost", 8001));
    socket.setSendGracefully(true);
    socket.addListener(getListener());
    System.out.println("Connecting");
    socket.connect();

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

  private static WebSocketAdapter getListener() {
    return new WebSocketAdapter() {
      @Override
      public boolean onOpen(final WebSocket webSocket) {
        Gdx.app.log("WS", "Connected!");
        webSocket.send("Hello from client!");
        return FULLY_HANDLED;
      }

      @Override
      public boolean onClose(final WebSocket webSocket, final int code, final String reason) {
        Gdx.app.log("WS", "Disconnected - status: " + code + ", reason: " + reason);
        return FULLY_HANDLED;
      }

      @Override
      public boolean onMessage(final WebSocket webSocket, final String packet) {
        Gdx.app.log("WS", "Got message: " + packet);
        return FULLY_HANDLED;
      }

      @Override
      public boolean onError(WebSocket webSocket, Throwable error) {
        System.out.println("Error");
        return super.onError(webSocket, error);
      }
    };
  }
}
