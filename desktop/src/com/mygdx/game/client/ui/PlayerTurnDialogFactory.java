package com.mygdx.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@Singleton
public class PlayerTurnDialogFactory {

  private final GameScreenAssets assets;
  private final Stage stage;
  private final PlayerInfo playerInfo;
  private final WebSocketHandler handler;

  @Inject
  public PlayerTurnDialogFactory(
      GameScreenAssets assets,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      PlayerInfo playerInfo,
      WebSocketHandler handler
  ) {
    this.assets = assets;
    this.stage = stage;
    this.playerInfo = playerInfo;
    this.handler = handler;
  }

  public void initializeHandler() {
    handler.registerHandler(ChangeTurnMessage.class, ((webSocket, o) -> {
      var changeTurnMessage = (ChangeTurnMessage) o;
      if (changeTurnMessage.getPlayerToken().equals(playerInfo.getToken())) {
        playerInfo.activatePlayer();
        createAndShow();
      } else {
        playerInfo.deactivatePlayer();
      }
      return FULLY_HANDLED;
    }));
  }

  public void createAndShow() {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = new Dialog("Turn", skin);
    dialog.text("Your turn have started");
    dialog.button("OK");
    dialog.show(stage);
  }

}
