package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.di.GameScreenSubcomponent;
import com.mygdx.game.client.di.Names;
import com.mygdx.game.client.ui.PlayerRoomDialogFactory;
import com.mygdx.game.client_core.network.GameStartService;
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

  @Inject
  GameRoomScreen(
      @NonNull @Named(Names.SCREEN_VIEWPORT) Stage stage,
      @NonNull GameScreenSubcomponent.Builder gameScreenBuilder,
      @NonNull GameStartService gameStartService,
      @NonNull PlayerRoomDialogFactory roomDialogFactory
  ) {
    this.stage = stage;
    this.gameScreenBuilder = gameScreenBuilder;
    this.gameStartService = gameStartService;
    this.roomDialogFactory = roomDialogFactory;
  }

  @Override
  public void show() {
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + "gameroomscreen shown");
    roomDialogFactory.create(() -> {
      var gameScreen = gameScreenBuilder.build().get();
      gameScreen.changeToGameScreen();
      gameStartService.startGame(5, 5, 401);
    }).show(stage);
    Gdx.input.setInputProcessor(stage);
    super.show();
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
}
