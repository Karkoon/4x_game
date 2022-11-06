package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.di.gameinstance.GameScreenSubcomponent;
import com.mygdx.game.client.hud.GameRoomHUD;
import com.mygdx.game.client_core.model.NetworkJobsQueueJobJobberManager;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.client_core.network.service.GameStartService;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Log
@Singleton
public class GameRoomScreen extends ScreenAdapter {

  private final GameRoomHUD gameRoomHUD;
  private final Stage stage;
  private final NetworkJobsQueueJobJobberManager jobManager;


  @Inject
  GameRoomScreen(
      @Named(StageModule.SCREEN_STAGE) Stage stage,
      GameRoomHUD gameRoomHUD,
      GameScreenSubcomponent.Builder gameScreenBuilder,
      GameStartService gameStartService,
      QueueMessageListener connection,
      NetworkJobsQueueJobJobberManager jobManager
  ) {
    this.stage = stage;
    this.gameRoomHUD = gameRoomHUD;
    this.jobManager = jobManager;
  }

  @Override
  public void show() {
    log.info("gameroomscreen shown");
    Gdx.input.setInputProcessor(stage);
    gameRoomHUD.prepareHudSceleton();
    log.info("initialized gameroomscreen");
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    stage.draw();
    gameRoomHUD.draw();
    stage.act(delta);
    gameRoomHUD.act(delta);
    jobManager.doAllJobs();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    stage.getViewport().update(width, height, true);
  }
}
