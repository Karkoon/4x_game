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
public class OutOfMoveRangeDialogFactory {

  private final GameScreenAssets assets;
  private final Stage stage;

  @Inject
  public OutOfMoveRangeDialogFactory(
      GameScreenAssets assets,
      @Named(StageModule.GAME_SCREEN) Stage stage
  ) {
    this.assets = assets;
    this.stage = stage;
  }

  public void createAndShow(int range, int distance) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = new Dialog("MoveRange", skin);
    dialog.text("You have " + range + " move points left!" + "You can't move " + distance + " units!");
    dialog.button("OK");
    dialog.show(stage);
  }


}
