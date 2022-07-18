package com.mygdx.game.client.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.di.StageModule;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.logging.Level;

@Singleton
@Log
public class TechnologyScreen extends ScreenAdapter {

  private final Stage stage;
  private final GdxGame game;
  private final ExtendViewport viewport;

  @Inject
  public TechnologyScreen(
          @NonNull @Named(StageModule.GAME_SCREEN) Stage stage,
          @NonNull GdxGame game
          ) {
    this.stage = stage;
    this.game = game;
    this.viewport = setUpCamera();
  }

  @Override
  public void show() {
    log.info("SubArea shown");

    positionCamera(viewport.getCamera());
    setUpInput();
  }

  @Override
  public void render(float delta) {
    viewport.getCamera().update();
    stage.draw();
    stage.act(delta);
  }

  private ExtendViewport setUpCamera() {
    log.log(Level.INFO, "provided viewport");
    var camera = new OrthographicCamera(500, 500);
    camera.near = 500f;
    camera.far = 700f;
    return new ExtendViewport(500, 500, camera);
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 600, 0);
    camera.lookAt(0, 0, 0);
  }

  private void setUpInput() {
  }
}
