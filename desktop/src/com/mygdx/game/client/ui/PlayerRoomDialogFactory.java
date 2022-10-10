package com.mygdx.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class PlayerRoomDialogFactory {

  private final GameScreenAssets assets;
  private final Stage stage;
  private final QueueMessageListener listener;
  private final ActiveToken activeToken;

  @Inject
  public PlayerRoomDialogFactory(
      GameScreenAssets assets,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      QueueMessageListener listener,
      ActiveToken activeToken
  ) {
    this.assets = assets;
    this.stage = stage;
    this.listener = listener;
    this.activeToken = activeToken;
  }

  public void createAndShow(@NonNull OnClose onClose) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = createDialog(skin, onClose);
    var numberOfPlayersLabel = new Label("0", skin);
    listener.registerHandler(PlayerJoinedRoomMessage.class, ((webSocket, o) -> {
      var message = (PlayerJoinedRoomMessage) o;
      numberOfPlayersLabel.setText(message.getNumberOfClients());
      log.info("A player joined the room: number_of_clients=" + message);
      return FULLY_HANDLED;
    }));
    listener.registerHandler(GameStartedMessage.class, ((webSocket, o) -> {
      var message = (GameStartedMessage) o;
      log.info("GameStartedMessage received");
      activeToken.setActiveToken(message.getPlayerToken());
      dialog.hide();
      return FULLY_HANDLED;
    }));
    dialog.text(numberOfPlayersLabel);
    dialog.button("Start game");
    log.info("shown PlayerRoomDialog");
    dialog.show(stage);
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
