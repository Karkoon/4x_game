package com.mygdx.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class PlayerAlreadyInTheRoomDialogFactory {

  private final GameScreenAssets assets;

  @Inject
  public PlayerAlreadyInTheRoomDialogFactory(
      GameScreenAssets assets
  ) {
    this.assets = assets;
  }

  public Dialog createAndShow() {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = new Dialog("Turn", skin);
    dialog.text("There is player with this nickname in the room");
    dialog.button("OK");
    return dialog;
  }
}
