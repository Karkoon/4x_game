package com.mygdx.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class PlayerRoomDialogFactory {
  private final GameScreenAssets assets;
  private final WebSocketHandler handler;

  @Inject
  public PlayerRoomDialogFactory(
      @NonNull GameScreenAssets assets,
      @NonNull WebSocketHandler handler
  ) {
    this.assets = assets;
    this.handler = handler;
  }

  public Dialog create(@NonNull OnClose onClose) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = createDialog(skin, onClose);
    var numberOfPlayersLabel = new Label("0", skin);
    handler.registerHandler(PlayerJoinedRoomMessage.class, ((webSocket, o) -> {
      var message = (PlayerJoinedRoomMessage) o;
      numberOfPlayersLabel.setText(message.getNumberOfClients());
      log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "A player joined the room: number_of_clients=" + message);
      return FULLY_HANDLED;
    }));
    handler.registerHandler(GameStartedMessage.class, ((webSocket, o) -> {
      dialog.hide();
      return FULLY_HANDLED;
    }));
    dialog.text(numberOfPlayersLabel);
    dialog.button("Start game");
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "shown PlayerRoomDialog");
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
