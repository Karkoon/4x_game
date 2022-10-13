package com.mygdx.game.client.screen;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.hud.WorldHUD;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.client.input.ClickInputAdapter;
import com.mygdx.game.client.input.GameScreenUiInputAdapter;
import com.mygdx.game.client.ui.PlayerRoomDialogFactory;
import com.mygdx.game.client.ui.PlayerTurnDialogFactory;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.service.GameConnectService;
import com.mygdx.game.client_core.network.service.GameStartService;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.util.CompositeUpdatable;
import com.mygdx.game.core.util.PositionUtil;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Log
@Singleton
public class GameScreen extends ScreenAdapter {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final World world;
  private final Viewport viewport;
  private final ModelInstanceRenderer renderer;

  private final Stage stage;
  private final WorldHUD worldHUD;
  private final ClickInputAdapter clickInputAdapter;
  private final GameScreenUiInputAdapter gameScreenUiInputAdapter;
  private final GameStartService gameStartService;
  private final PlayerRoomDialogFactory roomDialogFactory;
  private final GameConnectService gameConnectService;
  private final PlayerTurnDialogFactory playerTurnDialogFactory;
  private final PlayerInfo playerInfo;

  private boolean initialized = false;

  @AspectDescriptor(all = {Movable.class, Owner.class, Coordinates.class})
  private EntitySubscription unitsWithOwner;
  private ComponentMapper<Owner> ownerMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;

  @Inject
  public GameScreen(
      ModelInstanceRenderer renderer,
      World world,
      Viewport viewport,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      WorldHUD worldHUD,
      ClickInputAdapter clickInputAdapter,
      GameScreenUiInputAdapter gameScreenUiInputAdapter,
      GameStartService gameStartService,
      PlayerRoomDialogFactory roomDialogFactory,
      GameConnectService gameConnectService,
      PlayerTurnDialogFactory playerTurnDialogFactory,
      PlayerInfo playerInfo
  ) {
    this.renderer = renderer;
    this.world = world;
    this.viewport = viewport;
    this.stage = stage;
    this.worldHUD = worldHUD;
    this.gameScreenUiInputAdapter = gameScreenUiInputAdapter;
    this.clickInputAdapter = clickInputAdapter;
    this.gameStartService = gameStartService;
    this.roomDialogFactory = roomDialogFactory;
    this.gameConnectService = gameConnectService;
    this.playerTurnDialogFactory = playerTurnDialogFactory;
    this.playerInfo = playerInfo;
    this.world.inject(this);
  }

  @Override
  public void show() {
    log.info("GameScreen shown");
    if (!initialized) {
      playerTurnDialogFactory.initializeHandler();
      roomDialogFactory.createAndShow(() -> gameStartService.startGame(10, 10, GameConfigs.MAP_TYPE_MIN));
      gameConnectService.connect();
      setUpUnitWithOwnerListener();
      initialized = true;
    }
    setUpInput();
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);
    world.setDelta(delta);
    world.process();
    viewport.getCamera().update();

    stage.draw();
    worldHUD.draw();

    stage.act(delta);
    worldHUD.act(delta);
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
    disposeInput();
  }

  private void setUpInput() {
    var cameraInputProcessor = new CameraMoverInputProcessor(viewport);
    var inputMultiplexer = new InputMultiplexer(cameraInputProcessor, gameScreenUiInputAdapter, stage, clickInputAdapter);
    compositeUpdatable.addUpdatable(cameraInputProcessor.getCameraControl());
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  private void disposeInput() {
    Gdx.input.setInputProcessor(null);
  }


  private void setUpUnitWithOwnerListener() {
    unitsWithOwner.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
      @Override
      public void inserted(IntBag entities) {
        centerCameraOnOwnersUnit(entities);
        unitsWithOwner.removeSubscriptionListener(this);
      }

      @Override
      public void removed(IntBag entities) {
        // empty
      }
    });
  }

  private void centerCameraOnOwnersUnit(IntBag entities) {
    for (int i = 0; i < entities.size(); i++) {
      var unit = entities.get(i);
      var unitToken = ownerMapper.get(unit).getToken();
      if (unitToken.equals(playerInfo.getToken())) {
        var cameraHeight = viewport.getCamera().position.y;
        var unitPosition = PositionUtil.generateWorldPositionForCoords(coordinatesMapper.get(unit));
        viewport.getCamera().position.set(unitPosition.x, cameraHeight, unitPosition.z);
        break;
      }
    }
  }
}
