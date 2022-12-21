package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.hud.GameRoomScreenHUD;
import com.mygdx.game.client.ui.decorations.StarBackground;
import com.mygdx.game.client_core.model.NetworkJobsQueueJobJobberManager;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Log
@Singleton
public class GameRoomScreen extends ScreenAdapter {

  private final GameRoomScreenHUD gameRoomScreenHUD;
  private final Stage stage;
  private final NetworkJobsQueueJobJobberManager jobManager;
  private final StarBackground starBackground;


  @Inject
  GameRoomScreen(
      @Named(StageModule.SCREEN_STAGE) Stage stage,
      GameRoomScreenHUD gameRoomScreenHUD,
      NetworkJobsQueueJobJobberManager jobManager,
      StarBackground starBackground
  ) {
    this.stage = stage;
    this.gameRoomScreenHUD = gameRoomScreenHUD;
    this.jobManager = jobManager;
    this.starBackground = starBackground;
  }

  @Override
  public void show() {
    log.info("gameroomscreen shown");
    Gdx.input.setInputProcessor(stage);
    gameRoomScreenHUD.prepareHudSceleton();
    log.info("initialized gameroomscreen");
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    stage.draw();

    starBackground.update(delta);
    stage.getBatch().begin();
    starBackground.draw(stage.getBatch(), stage.getCamera());
    stage.getBatch().end();

    gameRoomScreenHUD.draw();

    stage.act(delta);
    gameRoomScreenHUD.act(delta);
    jobManager.doAllJobs();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    starBackground.resize(width, height);
    stage.getViewport().update(width, height, true);
    gameRoomScreenHUD.resize(width, height);
  }
}
