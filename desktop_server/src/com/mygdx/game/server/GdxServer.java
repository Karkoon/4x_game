package com.mygdx.game.server;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.server.network.Server;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class GdxServer extends Game {

  private final AllAssets assets;
  private final Server server;

  @Inject
  GdxServer(AllAssets assets,
            Server server) {
    this.assets = assets;
    this.server = server;
  }

  @Override
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
*/
    log.info("Loading assets...");
    assets.loadAssetsSync();
    log.info("Assets loaded.");

    log.info("Starting server.");
    server.runServer();
  }

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
}
