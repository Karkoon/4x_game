package com.mygdx.game.bot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.bot.screen.GameRoomScreen;
import com.mygdx.game.client_core.network.service.GameConnectService;
import dagger.Lazy;

import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Log
@Singleton
public class GdxGame extends Game {


  private final GameConfigAssets assets;
  private final Lazy<GameRoomScreen> gameRoomScreen;

  @Inject
  GdxGame(
    GameConfigAssets assets,
    Lazy<GameRoomScreen> gameRoomScreen,
    GameConnectService gameConnectService
  ) {
    this.assets = assets;
    this.gameRoomScreen = gameRoomScreen;
    this.gameConnectService = gameConnectService;
  }
  private final GameConnectService gameConnectService;

  @Override
  public void create() {
    log.info("Loading assets...");
    assets.loadAssetsSync();
    log.info("Assets loaded.");
    gameConnectService.connect(gameRoomName, "bot" + "_" + UUID.randomUUID()); //todo get the game room name from Args

    changeToGameRoomScreen();
  }
  private String gameRoomName = "defaultRoom"; // default value from the desktop client
  // to make it easier to connect to the same room at this point in time
  @Override
  public void render() {
    ScreenUtils.clear(0f, 0f, 0f, 1, true);
    super.render();
  }

  @Override
  public void dispose() {
    super.dispose();
    Gdx.app.exit();
  }

  public void changeToGameRoomScreen() {
    setScreen(gameRoomScreen.get());
  }
}
