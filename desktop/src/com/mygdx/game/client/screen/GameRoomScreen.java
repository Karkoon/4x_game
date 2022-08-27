package com.mygdx.game.client.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.di.GameScreenSubcomponent;
import com.mygdx.game.client.ui.PlayerRoomDialogFactory;
import com.mygdx.game.client_core.network.GameStartService;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameRoomScreen extends ScreenAdapter {

  private final Viewport viewport;
  private final Stage stage;
  private final GameScreenSubcomponent.Builder gameScreenBuilder;
  private final GameStartService gameStartService;
  private final PlayerRoomDialogFactory roomDialogFactory;

  @Inject
  GameRoomScreen(
      @NonNull Viewport viewport,
      @NonNull Stage stage,
      @NonNull GameScreenSubcomponent.Builder gameScreenBuilder,
      @NonNull GameStartService gameStartService,
      @NonNull PlayerRoomDialogFactory roomDialogFactory
  ) {
    this.viewport = viewport;
    this.stage = stage;
    this.gameScreenBuilder = gameScreenBuilder;
    this.gameStartService = gameStartService;
    this.roomDialogFactory = roomDialogFactory;
  }

  @Override
  public void show() {
    roomDialogFactory.createAndShow(() -> {
      var gameScreen = gameScreenBuilder.build().get();
      gameScreen.changeToGameScreen();
      gameStartService.startGame(5, 5, 10);
    });
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    stage.act(delta);
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    viewport.update(width, height);
  }
}
