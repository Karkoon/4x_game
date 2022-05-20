package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameEngine;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.CompositeUpdatable;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.entityfactory.FieldFactory;
import com.mygdx.game.client.entityfactory.UnitFactory;
import com.mygdx.game.client.initialize.MapInitializer;
import com.mygdx.game.client.initialize.StartUnitInitializer;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.client.input.GameScreenInputAdapter;
import com.mygdx.game.client.model.ActiveEntity;
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
  private final UnitFactory unitFactory;

  private final ModelInstanceRenderer renderer;

  private final GameState gameState;
  private final ActiveEntity activeEntity;

  private Stage stage;

  @Inject
  public GameScreen(@NonNull GameScreenAssets assets,
                    @NonNull ModelInstanceRenderer renderer,
                    @NonNull GameEngine engine,
                    @NonNull Viewport viewport,
                    @NonNull FieldFactory fieldFactory,
                    @NonNull UnitFactory unitFactory,
                    @NonNull GameState gameState,
                    @NonNull ActiveEntity activeEntity) {
    this.assets = assets;
    this.engine = engine;
    this.renderer = renderer;
    this.viewport = viewport;
    this.fieldFactory = fieldFactory;
    this.unitFactory = unitFactory;
    this.gameState = gameState;
    this.activeEntity = activeEntity;
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 300, 0);
    camera.lookAt(0, 0, 0);
  }

  @Override
  public void show() {
    positionCamera(viewport.getCamera());

    stage = new Stage(new ScreenViewport());

    compositeUpdatable.addUpdatable(engine);

    gameState.setFieldList(MapInitializer.initializeMap(fieldFactory, assets));
    StartUnitInitializer.initializeTestUnit(unitFactory, assets, gameState.getFieldList());

    var inputProcessor = new CameraMoverInputProcessor(viewport);
    var gameScreenInput = new GameScreenInputAdapter(viewport, gameState, stage, assets, activeEntity);

    InputMultiplexer inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(inputProcessor);
    inputMultiplexer.addProcessor(stage);
    inputMultiplexer.addProcessor(gameScreenInput);

    compositeUpdatable.addUpdatable(inputProcessor.getCameraControl());
    Gdx.input.setInputProcessor(inputMultiplexer);

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
}
