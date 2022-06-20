package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.di.component.SingleGameComponent;
import com.mygdx.game.client.di.module.StageModule;
import com.mygdx.game.client.di.scope.GameScreenScope;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.client.input.RestartGameInput;
import com.mygdx.game.client.model.Lifecycle;
import com.mygdx.game.client.model.SingleGame;
import com.mygdx.game.client.ui.PlayerRoomDialogFactory;
import com.mygdx.game.core.util.CompositeUpdatable;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

import static com.mygdx.game.client.model.Lifecycle.State.SETUP;
import static com.mygdx.game.client.model.Lifecycle.State.START;

@Log
@GameScreenScope
public class GameScreen extends ScreenAdapter implements Lifecycle.LifecycleObserver {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final Viewport viewport;
  private final Stage stage;
  private final PlayerRoomDialogFactory roomDialogFactory;
  private final WebSocket websocket;
  private final SingleGameComponent.Factory singleGameComponentFactory;
  private final ModelInstanceRenderer renderer;
  private final RestartGameInput restartGameInput;
  private SingleGame currentGame;

  @Inject
  public GameScreen(
      @NonNull Viewport viewport,
      @NonNull @Named(StageModule.GAME_SCREEN) Stage stage,
      @NonNull PlayerRoomDialogFactory roomDialogFactory,
      @NonNull WebSocket websocket,
      @NonNull SingleGameComponent.Factory singleGameComponentFactory,
      @NonNull ModelInstanceRenderer renderer,
      @NonNull RestartGameInput restartGameInput
  ) {
    this.viewport = viewport;
    this.stage = stage;
    this.roomDialogFactory = roomDialogFactory;
    this.websocket = websocket;
    this.singleGameComponentFactory = singleGameComponentFactory;
    this.renderer = renderer;
    this.restartGameInput = restartGameInput;
  }

  @Override
  public void show() {
    log.info("GameScreen shown");
    currentGame = singleGameComponentFactory.get().singleGame();
    currentGame.getLifecycle().subscribe(this);
    currentGame.getLifecycle().changeState(SETUP);
  }

  @Override
  public void render(float delta) { // TODO: 17.06.2022 change depending on lifecyclestate
    ScreenUtils.clear(0f, 0f, 0f, 1, true);
    compositeUpdatable.update(delta);
    currentGame.process(delta);
    viewport.getCamera().update();
    stage.draw();
    stage.act(delta);
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void dispose() {
    //lifecycle.unsubscribe(this);
  }

  private void setUpInput() {
    var cameraInputProcessor = new CameraMoverInputProcessor(viewport);
    var inputMultiplexer = new InputMultiplexer(cameraInputProcessor, stage, restartGameInput);
    compositeUpdatable.addUpdatable(cameraInputProcessor.getCameraControl());
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 600, 0);
    camera.lookAt(0, 0, 0);
  }

  @Override
  public void onStateChanged(Lifecycle.State state, Lifecycle lifecycle) {
    switch (state) {
      case SETUP -> handleSetup();
      case END -> handleEnd();
    }
  }

  private void handleSetup() {
    currentGame.registerHandlers();
    Dialog roomDialog = roomDialogFactory.create(() -> currentGame.getLifecycle().changeState(START));
    setUpInput();
    positionCamera(viewport.getCamera());
    websocket.send("connect");
    roomDialog.show(stage);
  }

  private void handleStart() {

  }

  private void handleEnd() {
    websocket.send("restart");
    currentGame.getLifecycle().changeState(SETUP);
    currentGame.getLifecycle().unsubscribe(this);
    currentGame = singleGameComponentFactory.get().singleGame();
  }
}
