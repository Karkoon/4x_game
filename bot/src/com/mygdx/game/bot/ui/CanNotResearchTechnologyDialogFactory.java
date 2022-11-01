package com.mygdx.game.bot.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class CanNotResearchTechnologyDialogFactory {
  private final GameScreenAssets assets;

  @Inject
  public CanNotResearchTechnologyDialogFactory(
      GameScreenAssets assets
  ) {
    this.assets = assets;
  }

  public Dialog createAndShow(String message) {
    log.info("Create cannot research dialog");
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = new Dialog("Cannon research technology", skin);
    dialog.text(message);
    dialog.button("OK");
    dialog.setPosition(400, 200);
    dialog.setWidth(450);
    dialog.scaleBy(1.5f);
    return dialog;
  }
}
