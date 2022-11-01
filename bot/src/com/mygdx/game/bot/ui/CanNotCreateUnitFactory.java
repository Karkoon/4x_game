package com.mygdx.game.bot.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.bot.di.StageModule;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Log
@Singleton
public class CanNotCreateUnitFactory {
  private final GameScreenAssets assets;
  private final Stage stage;

  @Inject
  public CanNotCreateUnitFactory(
      GameScreenAssets assets,
      @Named(StageModule.FIELD_SCREEN) Stage stage
  ) {
    this.assets = assets;
    this.stage = stage;
  }

  public void createAndShow(String message) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = new Dialog("Create unit", skin);
    dialog.text(message);
    dialog.button("OK");
    dialog.show(stage);
  }
}
