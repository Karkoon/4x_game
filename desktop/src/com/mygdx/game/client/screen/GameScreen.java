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
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.hud.GameScreenHUD;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.client.input.ClickInputAdapter;
import com.mygdx.game.client.input.GameScreenUiInputAdapter;
import com.mygdx.game.client.model.WindowConfig;
import com.mygdx.game.client.ui.PlayerTurnDialogFactory;
import com.mygdx.game.client.ui.WinAnnouncementDialogFactory;
import com.mygdx.game.client.ui.decorations.StarBackground;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceNetworkModule;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.client_core.network.service.GameInterruptedService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.network.messages.GameInterruptedMessage;
import com.mygdx.game.core.util.CompositeUpdatable;
import com.mygdx.game.core.util.PositionUtil;
import dagger.Lazy;
import lombok.extern.java.Log;
import org.lwjgl.glfw.GLFW;

import javax.inject.Inject;
import javax.inject.Named;

@GameInstanceScope
@Log
public class GameScreen extends ScreenAdapter implements Navigator {

  private final ActiveToken activeToken;
  private final CameraMoverInputProcessor cameraInputProcessor;
  private final ClickInputAdapter clickInputAdapter;
  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();
  private final Lazy<FieldScreen> fieldScreen;
  private final GameScreenHUD gameScreenHUD;
  private final GameScreenUiInputAdapter gameScreenUiInputAdapter;
  private final GdxGame game;
  private final GameInterruptedService gameInterruptedService;
  private final ModelInstanceRenderer modelInstanceRenderer;
  private final PlayerInfo playerInfo;
  private final PlayerTurnDialogFactory playerTurnDialogFactory;
  private final Stage stage;
  private final StarBackground starBackground;
  private final Lazy<TechnologyScreen> technologyScreen;
  private final Viewport viewport;
  private final QueueMessageListener queueMessageListener;
  private final WinAnnouncementDialogFactory winAnnouncementDialogFactory;
  private final World world;

  private boolean initialized = false;

  @AspectDescriptor(all = {Movable.class, Owner.class, Coordinates.class})
  private EntitySubscription unitsWithOwner;

  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Owner> ownerMapper;

  @Inject
  public GameScreen(
      ActiveToken activeToken,
      ClickInputAdapter clickInputAdapter,
      GameScreenHUD gameScreenHUD,
      GameInterruptedService gameInterruptedService,
      GameScreenUiInputAdapter gameScreenUiInputAdapter,
      GdxGame game,
      Lazy<FieldScreen> fieldScreen,
      ModelInstanceRenderer modelInstanceRenderer,
      PlayerInfo playerInfo,
      PlayerTurnDialogFactory playerTurnDialogFactory,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      StarBackground starBackground,
      Lazy<TechnologyScreen> technologyScreen,
      @Named(GameInstanceNetworkModule.GAME_INSTANCE) QueueMessageListener queueMessageListener,
      Viewport viewport,
      WinAnnouncementDialogFactory winAnnouncementDialogFactory,
      World world
      ) {
    this.activeToken = activeToken;
    this.cameraInputProcessor = new CameraMoverInputProcessor(viewport);
    this.clickInputAdapter = clickInputAdapter;
    this.fieldScreen = fieldScreen;
    this.gameScreenHUD = gameScreenHUD;
    this.gameScreenUiInputAdapter = gameScreenUiInputAdapter;
    this.game = game;
    this.gameInterruptedService = gameInterruptedService;
    this.modelInstanceRenderer = modelInstanceRenderer;
    this.playerInfo = playerInfo;
    this.playerTurnDialogFactory = playerTurnDialogFactory;
    this.stage = stage;
    this.starBackground = starBackground;
    this.technologyScreen = technologyScreen;
    this.queueMessageListener = queueMessageListener;
    this.viewport = viewport;
    this.winAnnouncementDialogFactory = winAnnouncementDialogFactory;
    this.world = world;
    this.world.inject(this);
  }

  @Override
  public void show() {
    log.info("GameScreen shown");
    if (!initialized) {
      playerTurnDialogFactory.initializeHandler();
      winAnnouncementDialogFactory.initializeHandler();
      setUpUnitWithOwnerListener();
      queueMessageListener.registerHandler(GameInterruptedMessage.class, ((webSocket, message) -> {
        exit();
        return true;
      }));
      initialized = true;
    }
    setUpInput();
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);

    starBackground.update(delta);
    stage.getBatch().begin();
    starBackground.draw(stage.getBatch(), stage.getCamera());
    stage.getBatch().end();


    world.setDelta(delta);
    world.process();
    viewport.getCamera().update();

    stage.draw();
    gameScreenHUD.draw();

    stage.act(delta);
    gameScreenHUD.act(delta);
    if (GLFW.glfwGetWindowAttrib(WindowConfig.windowHandle, GLFW.GLFW_FOCUSED) == 0)
      cameraInputProcessor.stopMoving();
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    stage.getViewport().update(width, height, true);
    gameScreenHUD.resize();
    starBackground.resize(width, height);

  }

  @Override
  public void dispose() {
    modelInstanceRenderer.dispose();
  }

  @Override
  public void hide() {
    disposeInput();
  }

  private void setUpInput() {
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

  public void changeTo(Direction screenDirection) {
    switch (screenDirection) {
      case GAME_SCREEN -> changeToGameScreen();
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
    game.changeToGameRoomScreen();
    gameInterruptedService.sendInterruptNotification();
  }

  public void setActivePlayerToken(String playerToken) {
    activeToken.setActiveToken(playerToken);
  }
}
