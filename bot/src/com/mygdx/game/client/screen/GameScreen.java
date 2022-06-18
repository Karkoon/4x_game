package com.mygdx.game.client.screen;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.bot.BotClient;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.initialize.MapService;
import com.mygdx.game.client.initialize.UnitService;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.client.input.MoveEntityInputAdapter;
import com.mygdx.game.core.util.CompositeUpdatable;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class GameScreen extends ScreenAdapter {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final World world;
  private final Viewport viewport;
  private final ModelInstanceRenderer renderer;

  private final Stage stage;
  private final MoveEntityInputAdapter moveEntityInputAdapter;
  private final MapService mapService;
  private final UnitService unitService;

  private final BotClient botClient;

  @Inject
  public GameScreen(
      @NonNull ModelInstanceRenderer renderer,
      @NonNull World world,
      @NonNull Viewport viewport,
      @NonNull @Named(StageModule.GAME_SCREEN) Stage stage,
      @NonNull MoveEntityInputAdapter moveEntityInputAdapter,
      @NonNull MapService mapService,
      @NonNull UnitService unitService,
      @NonNull BotClient botClient
  ) {
    this.renderer = renderer;
    this.world = world;
    this.viewport = viewport;
    this.stage = stage;
    this.moveEntityInputAdapter = moveEntityInputAdapter;
    this.mapService = mapService;
    this.unitService = unitService;
    this.botClient = botClient;
  }

  @Override
  public void show() {
    log.info("GameScreen shown");
    positionCamera(viewport.getCamera());
    setUpInput();
    mapService.initializeMap();
    unitService.initializeTestUnit();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        botClient.run();
      }
    });
    t1.start();
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);
    world.setDelta(delta);
    world.process();
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
    var inputMultiplexer = new InputMultiplexer(cameraInputProcessor, stage, moveEntityInputAdapter);
    compositeUpdatable.addUpdatable(cameraInputProcessor.getCameraControl());
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 600, 0);
    camera.lookAt(0, 0, 0);
  }
}
