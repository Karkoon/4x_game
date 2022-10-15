package com.mygdx.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class PlayerRoomDialogFactory {

  private final GameScreenAssets assets;
  private final QueueMessageListener listener;

  @Inject
  public PlayerRoomDialogFactory(
      GameScreenAssets assets,
      QueueMessageListener listener
  ) {
    this.assets = assets;
    this.listener = listener;
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

  public Dialog create(@NonNull OnClose onClose) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = createDialog(skin, onClose);
    var numberOfPlayersLabel = new Label("0", skin);
    listener.registerHandler(PlayerJoinedRoomMessage.class, ((webSocket, o) -> {
      numberOfPlayersLabel.setText(o.getNumberOfClients());
      log.info("A player joined the room: number_of_clients=" + o);
      return FULLY_HANDLED;
    }));
    listener.registerHandler(GameStartedMessage.class, ((webSocket, o) -> {
      dialog.hide();
      return FULLY_HANDLED;
    }));
    dialog.text(numberOfPlayersLabel);
    dialog.button("Start game");
    log.info("shown PlayerRoomDialog");
    return dialog;
  }
}
