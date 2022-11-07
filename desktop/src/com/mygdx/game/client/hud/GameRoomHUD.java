package com.mygdx.game.client.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.assets.MenuScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.di.gameinstance.GameScreenSubcomponent;
import com.mygdx.game.client.ui.PlayerAlreadyInTheRoomDialogFactory;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.client_core.network.service.GameConnectService;
import com.mygdx.game.client_core.network.service.GameStartService;
import com.mygdx.game.config.CivilizationConfig;
import com.mygdx.game.core.model.PlayerLobby;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.network.messages.PlayerAlreadyInTheRoomMessage;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class GameRoomHUD implements Disposable {

  private final GameConnectService connectService;
  private final GameConfigAssets gameConfigAssets;
  private final GameStartService gameStartService;
  private final GameScreenSubcomponent.Builder gameScreenBuilder;
  private final GdxGame game;
  private final MenuScreenAssets menuScreenAssets;
  private final Stage stage;
  private final Viewport viewport;
  private final PlayerInfo playerInfo;
  private final Skin menuScreenSkin;
  private final UiElementsCreator uiElementsCreator;
  private final QueueMessageListener queueMessageListener;
  private final PlayerAlreadyInTheRoomDialogFactory playerAlreadyInTheRoomDialogFactory;

  private List<PlayerLobby> players;
  private Table playerTable;
  private Button startButton;

  @Inject
  public GameRoomHUD(
      GameConnectService connectService,
      GameConfigAssets gameConfigAssets,
      GameStartService gameStartService,
      GameScreenSubcomponent.Builder gameScreenBuilder,
      GdxGame game,
      @Named(StageModule.SCREEN_STAGE) Stage stage,
      MenuScreenAssets menuScreenAssets,
      Viewport viewport,
      PlayerInfo playerInfo,
      UiElementsCreator uiElementsCreator,
      QueueMessageListener queueMessageListener,
      PlayerAlreadyInTheRoomDialogFactory playerAlreadyInTheRoomDialogFactory
  ) {
    this.connectService = connectService;
    this.gameConfigAssets = gameConfigAssets;
    this.gameStartService = gameStartService;
    this.gameScreenBuilder = gameScreenBuilder;
    this.game = game;
    this.stage = stage;
    this.viewport = viewport;
    this.playerInfo = playerInfo;
    this.menuScreenAssets = menuScreenAssets;
    this.uiElementsCreator = uiElementsCreator;
    this.queueMessageListener = queueMessageListener;
    this.playerAlreadyInTheRoomDialogFactory = playerAlreadyInTheRoomDialogFactory;

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
    queueMessageListener.registerHandler(PlayerAlreadyInTheRoomMessage.class, ((webSocket, message) -> {
      game.changeToGameRoomListScreen();
      playerAlreadyInTheRoomDialogFactory.createAndShow();
      return FULLY_HANDLED;
    }));
    queueMessageListener.registerHandler(PlayerJoinedRoomMessage.class, ((webSocket, o) -> {
      players = (o.getUsers());
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
    for (PlayerLobby player : players) {
      var singlePlayerTable = uiElementsCreator.createTable((float) 0, (float) 0);
      var label = uiElementsCreator.createLabel("Nickname: " + player.getUserName(), 0, 0);
      uiElementsCreator.addToTableRow(label, singlePlayerTable);

      var selectBox = uiElementsCreator.createSelectBox();
      var allCivs = gameConfigAssets.getGameConfigs().getAll(CivilizationConfig.class);
      selectBox.setItems(allCivs);
      selectBox.setSelected(gameConfigAssets.getGameConfigs().get(CivilizationConfig.class, player.getCivId()));
      selectBox.addListener(new ChangeListener() {
        @Override
        public void changed (ChangeEvent event, Actor actor) {
          log.info(selectBox.getSelected().toString());
          playerInfo.setCivilization(((CivilizationConfig) selectBox.getSelected()).getId());
          connectService.reconnect();
        }
      });
      if (!player.getUserName().equals(playerInfo.getUserName()))
        selectBox.setDisabled(true);
      uiElementsCreator.addToTableRow(selectBox, singlePlayerTable);

      uiElementsCreator.addCellToTable(singlePlayerTable, playerTable);
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

  public void setPlayers(List<PlayerLobby> players) {
    this.players = players;
  }
}
