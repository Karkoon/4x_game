package com.mygdx.game.client.di;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

import javax.inject.Named;
import javax.inject.Singleton;

@Log
@Module
public class StageModule {

  private static final float VIRTUAL_WIDTH = 1920;
  private static final float VIRTUAL_HEIGHT = 1080;
  public static final String GAME_SCREEN = "game_screen";
  public static final String SCREEN_STAGE = "screen_viewport";

  @Provides
  public Stage providesStage() {
    var viewport = new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
    var stage = new Stage(viewport);
    stage.setDebugAll(true);
    log.info("provided Stage");
    return stage;
  }


  @Provides
  @Singleton
  @Named(GAME_SCREEN)
  public Stage providesGameStage() {
    var stage = new Stage(new ScreenViewport());
    stage.setDebugAll(true);
    log.info("provided GameScreen Stage");
    return stage;
  }

  public static final String FIELD_SCREEN = "field_screen";

  @Provides
  @Singleton
  @Named(FIELD_SCREEN)
  public Stage providesFieldStage() {
    var stage = new Stage(new ScreenViewport());
    stage.setDebugAll(true);
    log.info("provided FieldScreen Stage");
    return stage;
  }

  @Provides
  @Named(SCREEN_STAGE)
  public Stage providesGameRoomListScreen() {
    var stage = new Stage(new ScreenViewport());
    stage.setDebugAll(true);
    log.info("provided GameScreen Stage");
    return stage;
  }
}
