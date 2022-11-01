package com.mygdx.game.bot.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.bot.GdxGame;
import com.mygdx.game.bot.di.StageModule;
import com.mygdx.game.bot.ui.TextInputDialogFactory;
import com.mygdx.game.client_core.network.service.GameConnectService;

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
      @Named(StageModule.SCREEN_STAGE) Stage stage,
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
    textInputDialogFactory.create(
        "Room List",
        "Type room identifier",
        "Connect",
        "Return",
        positiveInput -> {
          game.changeToGameRoomScreen();
          connectService.connect(positiveInput);
        },
        () -> {

        }
    ).show(stage);
    Gdx.input.setInputProcessor(stage);
    super.show();
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    stage.getViewport().update(width, height, true);
  }
}
