package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.di.GameScreenSubcomponent;
import com.mygdx.game.client.di.Names;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.client.input.ClickInputAdapter;
import com.mygdx.game.client.input.GameScreenUiInputAdapter;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.GameInstance;
import com.mygdx.game.core.util.CompositeDisposable;
import com.mygdx.game.core.util.CompositeUpdatable;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@GameInstanceScope
@Log
public class GameScreen extends ScreenAdapter implements Navigator {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();
  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

  private final GdxGame game;
  private final Lazy<FieldScreen> fieldScreen;
  private final Lazy<TechnologyScreen> technologyScreen;
  private final Viewport viewport;

  private final Stage stage;
  private final ClickInputAdapter clickInputAdapter;
  private final GameScreenUiInputAdapter gameScreenUiInputAdapter;


  @Inject
  public GameScreen(
      @NonNull GdxGame game,
      @NonNull Lazy<FieldScreen> fieldScreen,
      @NonNull Lazy<TechnologyScreen> technologyScreen,
      @NonNull Lazy<GameInstance> gameInstance,
      @NonNull ModelInstanceRenderer renderer,
      @NonNull Viewport viewport,
      @NonNull @Named(Names.GAME_SCREEN) Stage stage,
      @NonNull ClickInputAdapter clickInputAdapter,
      @NonNull GameScreenUiInputAdapter gameScreenUiInputAdapter
  ) {
    this.game = game;
    this.fieldScreen = fieldScreen;
    this.technologyScreen = technologyScreen;
    this.viewport = viewport;
    this.stage = stage;
    this.gameScreenUiInputAdapter = gameScreenUiInputAdapter;
    this.clickInputAdapter = clickInputAdapter;
    this.compositeDisposable.addDisposable(renderer);
    this.compositeUpdatable.addUpdatable(delta -> renderer.render());
    this.compositeUpdatable.addUpdatable(delta -> gameInstance.get().update(delta));
    this.compositeUpdatable.addUpdatable(delta -> {
      stage.draw();
      stage.act(delta);
    });
  }

  @Override
  public void show() {
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "GameScreen shown");
    positionCamera(viewport.getCamera());
    setUpInput();
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);
    viewport.getCamera().update();
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void dispose() {
    compositeDisposable.dispose();
  }

  private void setUpInput() {
    var cameraInputProcessor = new CameraMoverInputProcessor(viewport);
    var inputMultiplexer = new InputMultiplexer(cameraInputProcessor, gameScreenUiInputAdapter, stage, clickInputAdapter);
    compositeUpdatable.addUpdatable(cameraInputProcessor.getCameraControl());
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 600, 0);
    camera.lookAt(0, 0, 0);
  }

  public void changeTo(Direction screenDirection) {
    switch (screenDirection) {
      case FIELD_SCREEN -> changeToFieldScreen();
      case TECHNOLOGY_SCREEN -> changeToTechnologyScreen();
      case EXIT -> exit();
    }
  }

  @Override
  public void changeToGameScreen() {
    game.setScreen(this);
  }

  public void changeToFieldScreen() {
    game.setScreen(fieldScreen.get());
  }

  public void changeToTechnologyScreen() {
    game.setScreen(technologyScreen.get());
  }
  @Override
  public void exit() {
    dispose();
    game.exit();
  }
}
