package com.mygdx.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client_core.network.ServerConnection;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class PlayerRoomDialogFactory {
  private final GameScreenAssets assets;
  private final ServerConnection serverConnection;

  @Inject
  public PlayerRoomDialogFactory(
      @NonNull GameScreenAssets assets,
      @NonNull ServerConnection serverConnection
  ) {
    this.assets = assets;
    this.serverConnection = serverConnection;
  }

  public Dialog create(@NonNull OnClose onClose) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = createDialog(skin, onClose);
    var numberOfPlayersLabel = new Label("0", skin);
    serverConnection.registerSingleMessageHandler(PlayerJoinedRoomMessage.class, ((webSocket, msg) -> {
      numberOfPlayersLabel.setText(msg.getNumberOfClients());
      log.info("A player joined the room: number_of_clients=" + msg);
      return FULLY_HANDLED;
    }));
    dialog.text(numberOfPlayersLabel);
    dialog.button("Start game");
    log.info("shown PlayerRoomDialog");
    return dialog;
  }


  private Dialog createDialog(Skin skin, OnClose onClose) {
    return new Dialog("Room", skin) {
      @Override
      protected void result(Object object) {
        // handler.removeListener(...)
        onClose.run();
        hide();
      }
    };
  }

  public interface OnClose extends Runnable {

  }
}
