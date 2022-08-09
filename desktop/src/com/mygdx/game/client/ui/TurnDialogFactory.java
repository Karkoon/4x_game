package com.mygdx.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client_core.network.EndTurnService;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Log
@Singleton
public class TurnDialogFactory {

  private final GameScreenAssets assets;
  private final Stage stage;

  @Inject
  public TurnDialogFactory(
      @NonNull GameScreenAssets assets,
      @NonNull @Named(StageModule.GAME_SCREEN) Stage stage
  ) {
    this.assets = assets;
    this.stage = stage;
  }

  public void notPlayerTurnDialog() {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = new Dialog("Turn", skin);
    dialog.text("Not your turn");
    dialog.button("OK");
    dialog.show(stage);
  }

}
