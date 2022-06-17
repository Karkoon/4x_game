package com.mygdx.game.server;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.assets.GameScreenAssets;

import javax.inject.Inject;

public class AllAssets {

  private final AssetManager manager;
  private final GameScreenAssets gameScreenAssets;

  @Inject
  public AllAssets(
      AssetManager manager,
      GameScreenAssets gameScreenAssets) {
    this.manager = manager;
    this.gameScreenAssets = gameScreenAssets;
  }

  void loadAssetsSync() {
    gameScreenAssets.loadAssetsAsync();
    manager.finishLoading();
  }

}
