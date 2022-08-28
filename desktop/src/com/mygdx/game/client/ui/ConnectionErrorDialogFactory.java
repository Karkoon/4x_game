package com.mygdx.game.client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.Names;
import com.mygdx.game.client.di.StageModule;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
// TODO: 04.06.2022 use it when server disconnects, it's s similar to a crash
// you have to first be able to detect when the server disconnects though
public class ConnectionErrorDialogFactory {
  private final GameScreenAssets assets;
  private final Stage stage;

  @Inject
  public ConnectionErrorDialogFactory(
      @NonNull GameScreenAssets assets,
      @NonNull @Named(Names.GAME_SCREEN) Stage stage
  ) {
    this.assets = assets;
    this.stage = stage;
  }

  public void createAndShow() {
    var dialog = createDialog();
    dialog.text("Connection error");
    dialog.button("OK");
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "shown ErrorConnectionDialog");
    dialog.show(stage);
  }


  private Dialog createDialog() {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    return new Dialog("Choose", skin) {
      @Override
      protected void result(Object object) {
        Gdx.app.exit();
      }
    };
  }
}
