package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.ecs.GameEngine;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.client.input.GameScreenInputAdapter;
import com.mygdx.game.client.modules.StageModule;
import com.mygdx.game.client.util.CompositeUpdatable;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class GameScreen extends ScreenAdapter {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final GameEngine engine;

  private final Viewport viewport;
  private final ModelInstanceRenderer renderer;


  private final Stage stage;
  private final GameScreenInputAdapter gameScreenInputAdapter;

  @Inject
  public GameScreen(@NonNull ModelInstanceRenderer renderer,
                    @NonNull GameEngine engine,
                    @NonNull Viewport viewport,
                    @NonNull @Named(StageModule.GAME_SCREEN) Stage stage,
                    @NonNull GameScreenInputAdapter gameScreenInputAdapter) {
    this.engine = engine;
    this.renderer = renderer;
    this.viewport = viewport;
    this.stage = stage;
    this.gameScreenInputAdapter = gameScreenInputAdapter;
  }

  @Override
  public void show() {
    log.info("GameScreen shown");
    positionCamera(viewport.getCamera());
    compositeUpdatable.addUpdatable(engine);
    setUpInput();
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);
    viewport.getCamera().update();
    renderer.render();
    stage.draw();
    stage.act(delta);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void dispose() {
    renderer.dispose();
  }

  private void setUpInput() {
    var cameraInputProcessor = new CameraMoverInputProcessor(viewport);
    var inputMultiplexer = new InputMultiplexer(cameraInputProcessor, stage, gameScreenInputAdapter);
    compositeUpdatable.addUpdatable(cameraInputProcessor.getCameraControl());
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 600, 0);
    camera.lookAt(0, 0, 0);
  }
}
