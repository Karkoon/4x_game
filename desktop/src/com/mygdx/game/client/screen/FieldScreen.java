package com.mygdx.game.client.screen;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.di.Names;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.client.input.SubFieldUiInputProcessor;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.core.util.CompositeUpdatable;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class FieldScreen extends ScreenAdapter {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final World world;
  private final Viewport viewport;
  private final ModelInstanceRenderer renderer;

  private final Stage stage;
  private final ChosenEntity chosenEntity;
  private final SubFieldUiInputProcessor subFieldUiInputProcessor;

  private int fieldParent = -1;

  @Inject
  public FieldScreen(
      @NonNull ModelInstanceRenderer renderer,
      @NonNull World world,
      @NonNull Viewport viewport,
      @NonNull @Named(Names.GAME_SCREEN) Stage stage,
      @NonNull ChosenEntity chosenEntity,
      @NonNull SubFieldUiInputProcessor subFieldUiInputProcessor
  ) {
    this.renderer = renderer;
    this.world = world;
    this.viewport = viewport;
    this.stage = stage;
    this.chosenEntity = chosenEntity;
    this.subFieldUiInputProcessor = subFieldUiInputProcessor;
  }

  @Override
  public void show() {
    log.info("SubArea shown");
    fieldParent = chosenEntity.pop();
    positionCamera(viewport.getCamera());
    setUpInput();
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);
    world.setDelta(delta);
    world.process();
    viewport.getCamera().update();
    renderer.subRender(fieldParent);
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

  @Override
  public void hide() {
    fieldParent = -1;
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 600, 0);
    camera.lookAt(0, 0, 0);
  }

  private void setUpInput() {
    var cameraInputProcessor = new CameraMoverInputProcessor(viewport);
    var inputMultiplexer = new InputMultiplexer(cameraInputProcessor, subFieldUiInputProcessor, stage);
    compositeUpdatable.addUpdatable(cameraInputProcessor.getCameraControl());
    Gdx.input.setInputProcessor(inputMultiplexer);
  }
}
