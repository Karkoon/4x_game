package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameEngine;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.CompositeUpdatable;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.entityfactory.FieldFactory;
import com.mygdx.game.client.initialize.MapInitializer;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.client.model.GameState;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameScreen extends ScreenAdapter {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final GameScreenAssets assets;
  private final GameEngine engine;

  private final Viewport viewport;
  private final FieldFactory fieldFactory;

  private final ModelInstanceRenderer renderer;

  private final GameState gameState;

  @Inject
  public GameScreen(@NonNull GameScreenAssets assets,
                    @NonNull ModelInstanceRenderer renderer,
                    @NonNull GameEngine engine,
                    @NonNull Viewport viewport,
                    @NonNull FieldFactory fieldFactory,
                    @NonNull GameState gameState) {
    this.assets = assets;
    this.engine = engine;
    this.renderer = renderer;
    this.viewport = viewport;
    this.fieldFactory = fieldFactory;
    this.gameState = gameState;
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 300, 0);
    camera.lookAt(0, 0, 0);
  }

  @Override
  public void show() {
    positionCamera(viewport.getCamera());

    compositeUpdatable.addUpdatable(engine);

    var inputProcessor = new CameraMoverInputProcessor(viewport);
    compositeUpdatable.addUpdatable(inputProcessor.getCameraControl());
    Gdx.input.setInputProcessor(inputProcessor);

    gameState.setFieldList(MapInitializer.initializeMap(fieldFactory, assets));
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);
    viewport.getCamera().update();
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
  }

  @Override
  public void dispose() {
    renderer.dispose();
  }
}
