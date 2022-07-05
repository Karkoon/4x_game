package com.mygdx.game.client.screen;

import com.artemis.World;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.input.MoveEntityInputAdapter;
import com.mygdx.game.client.network.GameStartService;
import com.mygdx.game.client.ui.PlayerRoomDialogFactory;
import com.mygdx.game.core.util.CompositeUpdatable;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class FieldScreen extends ScreenAdapter {

  public static Integer choosenField;
  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final World world;
  private final Viewport viewport;
  private final ModelInstanceRenderer renderer;

  private final Stage stage;
  private final MoveEntityInputAdapter moveEntityInputAdapter;
  private final GameStartService gameStartService;
  private final PlayerRoomDialogFactory roomDialogFactory;
  private final WebSocket webSocket;

  @Inject
  public FieldScreen(
          @NonNull ModelInstanceRenderer renderer,
          @NonNull World world,
          @NonNull Viewport viewport,
          @NonNull @Named(StageModule.GAME_SCREEN) Stage stage,
          @NonNull MoveEntityInputAdapter moveEntityInputAdapter,
          @NonNull GameStartService gameStartService,
          @NonNull PlayerRoomDialogFactory roomDialogFactory,
          @NonNull WebSocket webSocket
  ) {
    this.renderer = renderer;
    this.world = world;
    this.viewport = viewport;
    this.stage = stage;
    this.moveEntityInputAdapter = moveEntityInputAdapter;
    this.gameStartService = gameStartService;
    this.roomDialogFactory = roomDialogFactory;
    this.webSocket = webSocket;
  }

  @Override
  public void show() {
    log.info("SubArea shown");

    positionCamera(viewport.getCamera());
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

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 600, 0);
    camera.lookAt(0, 0, 0);
  }
}
