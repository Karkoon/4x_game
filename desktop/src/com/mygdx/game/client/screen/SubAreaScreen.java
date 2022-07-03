package com.mygdx.game.client.screen;

import com.artemis.World;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.core.util.CompositeUpdatable;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class SubAreaScreen extends ScreenAdapter {

  private Integer fieldIdentifier;

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final Viewport viewport;
  private final ModelInstanceRenderer renderer;
  private final World world;

  private final Stage stage;

  @Inject
  public SubAreaScreen(
      @NonNull Viewport viewport,
      @NonNull ModelInstanceRenderer renderer,
      @NonNull World world,
      @NonNull Stage stage
  ) {
    this.viewport = viewport;
    this.renderer = renderer;
    this.world = world;
    this.stage = stage;
  }

  @Override
  public void show() {
    log.info("SubArea shown");
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);
    world.setDelta(delta);
    world.process();
    viewport.getCamera().update();
    renderer.subRender();
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

}
