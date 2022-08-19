package com.mygdx.game.server;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.server.network.Server;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class GdxServer extends Game {

  private final GameConfigAssets assets;
  private final Server server;

  @Inject
  GdxServer(GameConfigAssets assets,
            Server server) {
    this.assets = assets;
    this.server = server;
  }

  @Override
  public void create() {
    log.info("Loading assets...");
    assets.loadAssetsSync();
    log.info("Assets loaded.");

    log.info("Starting server.");
    server.runServer();
  }

  @Override
  public void dispose() {
    super.dispose();
    Gdx.app.exit();
  }
}
