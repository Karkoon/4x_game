package com.mygdx.game.client.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.ui.PlayerRoomDialogFactory;
import com.mygdx.game.client_core.network.GameConnectService;
import com.mygdx.game.client_core.network.GameStartService;
import lombok.NonNull;

import javax.inject.Inject;

public class GameRoomScreen extends ScreenAdapter {

  private final Viewport viewport;
  private final Stage stage;
  private final GameStartService gameStartService;
  private final PlayerRoomDialogFactory roomDialogFactory;
  private final GameConnectService gameConnectService;

  @Inject
  GameRoomScreen(
      @NonNull Viewport viewport,
      @NonNull Stage stage,
      @NonNull GameStartService gameStartService,
      @NonNull PlayerRoomDialogFactory roomDialogFactory,
      @NonNull GameConnectService gameConnectService
  ) {
    this.viewport = viewport;
    this.stage = stage;
    this.gameStartService = gameStartService;
    this.roomDialogFactory = roomDialogFactory;
    this.gameConnectService = gameConnectService;
  }

  @Override
  public void show() {
    roomDialogFactory.createAndShow(() -> {
      gameStartService.startGame(5, 5, 10);
    });
    gameConnectService.connect();
  }

  private static class UiState {
    int mapWidth;
    int mapHeight;
    int playerName;
  }
}
