package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.client.di.GameScreenSubcomponent;
import com.mygdx.game.client.di.Names;
import com.mygdx.game.client.ui.PlayerRoomDialogFactory;
import com.mygdx.game.client_core.network.GameStartService;
import com.mygdx.game.client_core.network.ServerConnection;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.util.CompositeDisposable;
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
  private final ServerConnection connection;
  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Inject
  GameRoomScreen(
      @NonNull @Named(Names.SCREEN_VIEWPORT) Stage stage,
      @NonNull GameScreenSubcomponent.Builder gameScreenBuilder,
      @NonNull GameStartService gameStartService,
      @NonNull PlayerRoomDialogFactory roomDialogFactory,
      @NonNull ServerConnection connection
  ) {
    this.stage = stage;
    this.gameScreenBuilder = gameScreenBuilder;
    this.gameStartService = gameStartService;
    this.roomDialogFactory = roomDialogFactory;
    this.connection = connection;
  }

  @Override
  public void show() {
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + "gameroomscreen shown");
    roomDialogFactory.create(() -> gameStartService.startGame(5, 5, 401)).show(stage);
    compositeDisposable.add(connection.registerSingleMessageHandler(
        GameStartedMessage.class,
        (socket, packet) -> {
          log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " game started handled");
          Gdx.app.postRunnable(() -> {
            var gameScreen = gameScreenBuilder.build().get();
            gameScreen.changeToGameScreen();
          });
          return true;
        }
    ));
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    stage.draw();
    stage.act(delta);
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void hide() {
    compositeDisposable.dispose();
    super.hide();
  }
}
