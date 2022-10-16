package com.mygdx.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;
import static com.mygdx.game.client_core.di.gameinstance.GameInstanceNetworkModule.GAME_INSTANCE;

@Log
@GameInstanceScope
public class PlayerTurnDialogFactory {

  private final GameScreenAssets assets;
  private final Stage stage;
  private final PlayerInfo playerInfo;
  private final QueueMessageListener listener;

  @Inject
  public PlayerTurnDialogFactory(
      GameScreenAssets assets,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      PlayerInfo playerInfo,
      @Named(GAME_INSTANCE) QueueMessageListener listener
  ) {
    this.assets = assets;
    this.stage = stage;
    this.playerInfo = playerInfo;
    this.listener = listener;
  }

  public void initializeHandler() {
    listener.registerHandler(ChangeTurnMessage.class, ((webSocket, message) -> {
      if (message.getPlayerToken().equals(playerInfo.getToken())) {
        createAndShow();
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
