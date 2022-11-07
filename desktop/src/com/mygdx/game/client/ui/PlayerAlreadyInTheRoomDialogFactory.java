package com.mygdx.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Log
@Singleton
public class PlayerAlreadyInTheRoomDialogFactory {

  private final GameScreenAssets assets;
  private final Stage stage;

  @Inject
  public PlayerAlreadyInTheRoomDialogFactory(
      GameScreenAssets assets,
      @Named(StageModule.SCREEN_STAGE) Stage stage
  ) {
    this.assets = assets;
    this.stage = stage;
  }

  public void createAndShow() {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = new Dialog("Turn", skin);
    dialog.text("There is player with this nickname in the room");
    dialog.button("OK");
    dialog.show(stage);
  }
}
