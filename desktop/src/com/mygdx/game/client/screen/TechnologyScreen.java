package com.mygdx.game.client.screen;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.TextureRenderer;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.core.util.CompositeUpdatable;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class TechnologyScreen extends ScreenAdapter {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final World world;

  private final GdxGame game;
  private final Viewport viewport;
  private final TextureRenderer renderer;

  private final Stage stage;

  @Inject
  public TechnologyScreen(
          @NonNull GdxGame game,
          @NonNull World world,
          @NonNull @Named("orthographic") Viewport viewport,
          @NonNull TextureRenderer renderer,
          @NonNull Stage stage
          ) {
    this.game = game;
    this.world = world;
    this.viewport = viewport;
    this.renderer = renderer;
    this.stage = stage;
  }

  @Override
  public void show() {
    log.info("Technology tree shown");

    setUpInput();
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);
    world.setDelta(delta);
    world.process();
    viewport.getCamera().update();
    renderer.technologyRender();
//    stage.draw();
//    stage.act(delta);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
//    stage.getViewport().update(width, height, true);
  }

  private void setUpInput() {
    var cameraInputProcessor = new CameraMoverInputProcessor(viewport);
    var inputMultiplexer = new InputMultiplexer(cameraInputProcessor, stage);
    compositeUpdatable.addUpdatable(cameraInputProcessor.getCameraControl());
    Gdx.input.setInputProcessor(inputMultiplexer);
  }
}
