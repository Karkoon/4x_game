package com.mygdx.game.bot.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.bot.di.StageModule;
import com.mygdx.game.bot.di.gameinstance.GameScreenSubcomponent;
import com.mygdx.game.bot.ui.PlayerRoomDialogFactory;
import com.mygdx.game.client_core.model.NetworkJobsQueueJobJobberManager;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.client_core.network.service.GameStartService;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class GameRoomScreen extends ScreenAdapter {

  private final Stage stage;
  private final GameScreenSubcomponent.Builder gameScreenBuilder;
  private final GameStartService gameStartService;
  private final PlayerRoomDialogFactory roomDialogFactory;
  private final QueueMessageListener connection;
  private final NetworkJobsQueueJobJobberManager jobManager;

  private boolean initialized = false;
  private Dialog roomDialog;

  @Inject
  GameRoomScreen(
      @NonNull @Named(StageModule.SCREEN_STAGE) Stage stage,
      @NonNull GameScreenSubcomponent.Builder gameScreenBuilder,
      @NonNull GameStartService gameStartService,
      @NonNull PlayerRoomDialogFactory roomDialogFactory,
      @NonNull QueueMessageListener connection,
      NetworkJobsQueueJobJobberManager jobManager
  ) {
    this.stage = stage;
    this.gameScreenBuilder = gameScreenBuilder;
    this.gameStartService = gameStartService;
    this.roomDialogFactory = roomDialogFactory;
    this.connection = connection;
    this.jobManager = jobManager;
  }

  @Override
  public void show() {
    log.info("gameroomscreen shown");
    if (!initialized) {
      roomDialog = roomDialogFactory.create(() -> gameStartService.startGame(5, 5, 401));
      connection.registerHandler(
          GameStartedMessage.class,
          (socket, packet) -> {
            var gameScreen = gameScreenBuilder.build().get();
            gameScreen.setActivePlayerToken(packet.getPlayerToken());
            gameScreen.changeToGameScreen();
            return true;
          }
      );
      initialized = true;
      log.info("initialized gameroomscreen");
    }
    roomDialog.show(stage);
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    stage.draw();
    stage.act(delta);
    jobManager.doAllJobs();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    stage.getViewport().update(width, height, true);
  }
}
