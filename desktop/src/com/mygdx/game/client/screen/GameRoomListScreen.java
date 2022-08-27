package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.di.Names;
import com.mygdx.game.client.ui.TextInputDialogFactory;
import com.mygdx.game.client_core.network.GameConnectService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class GameRoomListScreen extends ScreenAdapter {

  private final Stage stage;
  private final TextInputDialogFactory textInputDialogFactory;
  private final GameConnectService connectService;
  private final GdxGame game;

  @Inject
  public GameRoomListScreen(
      @Named(Names.GAME_ROOM_LIST_SCREEN) Stage stage,
      TextInputDialogFactory textInputDialogFactory,
      GameConnectService connectService,
      GdxGame game
  ) {
    this.stage = stage;
    this.textInputDialogFactory = textInputDialogFactory;
    this.connectService = connectService;
    this.game = game;
  }

  @Override
  public void show() {
    textInputDialogFactory.createAndShow(
        "Room List",
        "Type room identifier",
        "Connect",
        "Return",
        positiveInput -> {
          connectService.connect(positiveInput);
          game.changeToGameRoomScreen();
        },
        () -> {

        }
    );
    Gdx.input.setInputProcessor(stage);
    super.show();
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    stage.act(delta);
    stage.draw();
  }
}
