package com.mygdx.game.client.modules;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

@Module
@Log
public class StageModule {

  private static final float VIRTUAL_WIDTH = 1920;
  private static final float VIRTUAL_HEIGHT = 1080;

  @Provides
  public Stage providesStage() {
    var viewport = new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
    var stage = new Stage(viewport);
    stage.setDebugAll(true);
    log.info("provided Stage");
    return stage;
  }
}
