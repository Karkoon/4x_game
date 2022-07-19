package com.mygdx.game.client.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.TextureRenderer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class TechnologyScreen extends ScreenAdapter {

  private final GdxGame game;
  private final Viewport viewport;
  private final TextureRenderer renderer;

  private final Stage stage;

  @Inject
  public TechnologyScreen(
          @NonNull GdxGame game,
          @NonNull @Named("orthographic") Viewport viewport,
          @NonNull TextureRenderer renderer,
          @NonNull Stage stage
          ) {
    this.game = game;
    this.viewport = viewport;
    this.renderer = renderer;
    this.stage = stage;
  }

  @Override
  public void show() {
    log.info("Technology tree shown");

    positionCamera(viewport.getCamera());
    setUpInput();
  }

  @Override
  public void render(float delta) {
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

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 600, 0);
    camera.lookAt(0, 0, 0);
  }

  private void setUpInput() {
  }
}
