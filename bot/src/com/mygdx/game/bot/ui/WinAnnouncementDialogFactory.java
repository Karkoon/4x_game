package com.mygdx.game.bot.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.bot.di.StageModule;
import com.mygdx.game.bot.screen.Navigator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.network.messages.WinAnnouncementMessage;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;
import static com.mygdx.game.client_core.di.gameinstance.GameInstanceNetworkModule.GAME_INSTANCE;

@Log
@GameInstanceScope
public class WinAnnouncementDialogFactory {

  private final GameScreenAssets assets;
  private final Stage stage;
  private final QueueMessageListener listener;
  private final Lazy<Navigator> navigator;

  @Inject
  public WinAnnouncementDialogFactory(
      GameScreenAssets assets,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      @Named(GAME_INSTANCE) QueueMessageListener listener,
      Lazy<Navigator> navigator
  ) {
    this.assets = assets;
    this.stage = stage;
    this.listener = listener;
    this.navigator = navigator;
  }

  public void initializeHandler() {
    listener.registerHandler(WinAnnouncementMessage.class, ((webSocket, message) -> {
      createAndShow(message.getWinnerName());
      return FULLY_HANDLED;
    }));
  }

  public void createAndShow(String winnerName) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var dialog = getDialog(skin);
    dialog.text("The winner is " + winnerName);
    dialog.button("OK");
    dialog.show(stage);
  }

  private Dialog getDialog(Skin skin) {
    return new Dialog("GAME FINISHED", skin) {
      @Override
      protected void result(Object object) {
        navigator.get().exit();
      }
    };
  }

}
