package com.mygdx.game.server;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class MyGdxGame extends Game {

  private final AssetManager assetManager;

  @Inject
  MyGdxGame(@NonNull AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  @Override
  // TODO: 04.06.2022 start server somehow
  public void create() {
/* TODO: 04.06.2022 todo loading assets must be separated between client and server,
  it's not game-breaking but it would make the server exec smaller */
    /*    loadAll();
    listenForConnections();
    createLobby();
    waitForPlayers();
    listenForGameParamsChanges();
    ifAllPlayersInLobbyAreReadyStartGame();
    sendTurnTokenToFirstPlayer();
    CommonWebSockets.initiate();

    // skądś wziąć zależności

    var server = new Server();
    server.connect();*/
  }

  @Override
  public void render() {
    ScreenUtils.clear((float) (Math.random() * 255f), 0f, 0f, 1, true);
    super.render();
  }

  @Override
  public void dispose() {
    super.dispose();
    assetManager.dispose();
    Gdx.app.exit();
  }
}
