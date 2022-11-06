package com.mygdx.game.client.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.MenuScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.di.gameinstance.GameScreenSubcomponent;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.client_core.network.service.GameStartService;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class GameRoomHUD implements Disposable {

  private final GameStartService gameStartService;
  private final GameScreenSubcomponent.Builder gameScreenBuilder;
  private final MenuScreenAssets menuScreenAssets;
  private final Stage stage;
  private final Viewport viewport;
  private final Skin menuScreenSkin;
  private final UiElementsCreator uiElementsCreator;
  private final QueueMessageListener queueMessageListener;

  private List<String> players;
  private Table playerTable;
  private Button startButton;

  @Inject
  public GameRoomHUD(
      GameStartService gameStartService,
      GameScreenSubcomponent.Builder gameScreenBuilder,
      @Named(StageModule.SCREEN_STAGE) Stage stage,
      MenuScreenAssets menuScreenAssets,
      Viewport viewport,
      UiElementsCreator uiElementsCreator,
      QueueMessageListener queueMessageListener
  ) {
    this.gameStartService = gameStartService;
    this.gameScreenBuilder = gameScreenBuilder;
    this.stage = stage;
    this.viewport = viewport;
    this.menuScreenAssets = menuScreenAssets;
    this.uiElementsCreator = uiElementsCreator;
    this.queueMessageListener = queueMessageListener;

    this.players = new ArrayList<>();
    menuScreenSkin = menuScreenAssets.getSkin(MenuScreenAssetPaths.SKIN);

    registerHandlers();
    prepareHudSceleton();
  }

  public void act(float delta) {
    stage.act(delta);
  }

  public void draw() {
    stage.draw();
  }

  public void dispose (){
    stage.dispose();
  }

  private void registerHandlers() {
    queueMessageListener.registerHandler(PlayerJoinedRoomMessage.class, ((webSocket, o) -> {
      players = (o.getUserNames());
      log.info("A player joined the room: name=" + o);
      prepareHudSceleton();
      return FULLY_HANDLED;
    }));
    queueMessageListener.registerHandler(GameStartedMessage.class, ((webSocket, o) -> {
      stage.clear();
      var gameScreen = gameScreenBuilder.build().get();
      gameScreen.setActivePlayerToken(o.getPlayerToken());
      gameScreen.changeToGameScreen();
      return FULLY_HANDLED;
    }));
  }

  public void prepareHudSceleton() {
    stage.clear();

    preparePlayerTable();
    prepareStartButton();

    Gdx.input.setInputProcessor(stage);
  }

  private void preparePlayerTable() {
    float width = stage.getWidth();
    float height = stage.getHeight();
    this.playerTable = uiElementsCreator.createTable((float) (width * 0.1), (float) (height * 0.1));
    uiElementsCreator.setActorWidthAndHeight(this.playerTable, (int) (width * 0.4), (int) (height * 0.8));
    for (String player : players) {
      var label = uiElementsCreator.createLabel("Nickname: " + player, 0, 0);
      uiElementsCreator.addCellToTable(label, playerTable);
    }
    stage.addActor(playerTable);
  }

  private void prepareStartButton() {
    float width = stage.getWidth();
    float height = stage.getHeight();
    this.startButton = uiElementsCreator.createActionButton("START", this::startGame, (int) (width * 0.8), (int) (height * 0.1));
    stage.addActor(startButton);
  }

  private void startGame() {
    gameStartService.startGame(5, 5, 401);
  }
}
