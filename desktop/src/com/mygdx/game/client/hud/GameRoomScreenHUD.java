package com.mygdx.game.client.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.di.gameinstance.GameScreenSubcomponent;
import com.mygdx.game.client.model.ChosenMapSize;
import com.mygdx.game.client.ui.PlayerAlreadyInTheRoomDialogFactory;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.client_core.network.service.GameConnectService;
import com.mygdx.game.client_core.network.service.GameStartService;
import com.mygdx.game.config.CivilizationConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.MapTypeConfig;
import com.mygdx.game.core.model.BotType;
import com.mygdx.game.core.model.MapSize;
import com.mygdx.game.core.model.PlayerLobby;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.network.messages.PlayerAlreadyInTheRoomMessage;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import com.mygdx.game.core.network.messages.RoomConfigMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class GameRoomScreenHUD implements Disposable {

  private final GameConfigAssets gameConfigAssets;
  private final GameConnectService gameConnectService;
  private final GameStartService gameStartService;
  private final GameScreenSubcomponent.Builder gameScreenBuilder;
  private final GdxGame game;
  private final PlayerInfo playerInfo;
  private final PlayerAlreadyInTheRoomDialogFactory playerAlreadyInTheRoomDialogFactory;
  private final Random random;
  private final Stage stage;
  private final UiElementsCreator uiElementsCreator;
  private final QueueMessageListener queueMessageListener;

  private List<PlayerLobby> players;
  private MapSize selectedMapSize;
  private MapTypeConfig selectedMapTypeConfig;


  private Window playerTable;
  private Window mapSizeWindow;
  private Window mapTypeWindow;
  private Button startButton;
  private Button exitButton;
  private Button addBotButton;

  @Inject
  public GameRoomScreenHUD(
      GameConnectService gameConnectService,
      GameConfigAssets gameConfigAssets,
      GameStartService gameStartService,
      GameScreenSubcomponent.Builder gameScreenBuilder,
      GdxGame game,
      @Named(StageModule.SCREEN_STAGE) Stage stage,
      PlayerInfo playerInfo,
      UiElementsCreator uiElementsCreator,
      QueueMessageListener queueMessageListener,
      PlayerAlreadyInTheRoomDialogFactory playerAlreadyInTheRoomDialogFactory
  ) {
    this.gameConnectService = gameConnectService;
    this.gameConfigAssets = gameConfigAssets;
    this.gameStartService = gameStartService;
    this.gameScreenBuilder = gameScreenBuilder;
    this.game = game;
    this.stage = stage;
    this.playerInfo = playerInfo;
    this.uiElementsCreator = uiElementsCreator;
    this.queueMessageListener = queueMessageListener;
    this.playerAlreadyInTheRoomDialogFactory = playerAlreadyInTheRoomDialogFactory;

    this.players = new ArrayList<>();
    this.selectedMapSize = MapSize.VERY_SMALL;
    this.random = new Random();
    this.selectedMapTypeConfig = gameConfigAssets.getGameConfigs().get(MapTypeConfig.class, randomBetween(GameConfigs.MAP_TYPE_MIN, GameConfigs.MAP_TYPE_MAX));
    registerHandlers();
    prepareHudSceleton();
  }

  public void act(float delta) {
    stage.act(delta);
  }

  public void draw() {
    stage.draw();
  }

  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    prepareHudSceleton();
  }

  public void dispose (){
    stage.dispose();
  }

  private void registerHandlers() {
    queueMessageListener.registerHandler(PlayerAlreadyInTheRoomMessage.class, ((webSocket, message) -> {
      var dialog = playerAlreadyInTheRoomDialogFactory.createAndShow();
      dialog.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          game.changeToGameRoomListScreen();
        }
      });
      dialog.show(stage);
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
    queueMessageListener.registerHandler(RoomConfigMessage.class, ((webSocket, o) -> {
      ChosenMapSize.mapSize = o.getMapSize();
      selectedMapSize = o.getMapSize();
      selectedMapTypeConfig = gameConfigAssets.getGameConfigs().get(MapTypeConfig.class, o.getMapType());
      log.info("Changed room config=" + o);
      prepareHudSceleton();
      return FULLY_HANDLED;
    }));
  }

  public void prepareHudSceleton() {
    stage.clear();

    preparePlayerTable();
    prepareMapSizeSelectBox();
    prepareMapTypeSelectBox();
    prepareStartButton();
    prepareExitButton();

    Gdx.input.setInputProcessor(stage);
  }

  private void preparePlayerTable() {
    this.playerTable = uiElementsCreator.createWindow("Player list");
    this.playerTable.setMovable(false);
    uiElementsCreator.setActorPosition(this.playerTable, (int) (stage.getWidth() * 0.05), (int) (stage.getHeight() * 0.4));
    uiElementsCreator.setActorWidthAndHeight(this.playerTable, (int) (stage.getWidth() * 0.9), (int) (stage.getHeight() * 0.55));
    var playerList = uiElementsCreator.createTable(0, 0);
    this.playerTable.add(playerList);
    for (PlayerLobby player : players) {
      var singlePlayerTable = uiElementsCreator.createTable((float) 0, (float) 0);
      var label = uiElementsCreator.createLabel(player.getUserName(), 0, 0);
      uiElementsCreator.addToTableRow(label, singlePlayerTable);

      var selectBox = uiElementsCreator.createSelectBox();
      var allCivs = gameConfigAssets.getGameConfigs().getAll(CivilizationConfig.class);
      selectBox.setItems(allCivs);
      selectBox.setSelected(gameConfigAssets.getGameConfigs().get(CivilizationConfig.class, player.getCivId()));
      if (player.getUserName().equals(playerInfo.getUserName())) {
        selectBox.addListener(new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            playerInfo.setCivilization(((CivilizationConfig) selectBox.getSelected()).getId());
            gameConnectService.changeUser();
          }
        });
      } else if (player.getBotType() != BotType.NOT_BOT) {
        selectBox.addListener(new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            gameConnectService.changeUser(player.getUserName(), ((CivilizationConfig) selectBox.getSelected()).getId(), player.getBotType().name());
          }
        });
      } else {
        selectBox.setDisabled(true);
      }
      uiElementsCreator.addToTableRow(selectBox, singlePlayerTable);
      if (player.getBotType() != BotType.NOT_BOT) {
        var botBox = uiElementsCreator.createSelectBox();
        Array<String> types = new Array<>();
        types.add(BotType.TRAINED.name());
        types.add(BotType.RANDOM_FIRST.name());
        botBox.setItems(types);
        botBox.setSelected(player.getBotType().name());
        botBox.addListener(new ChangeListener() {
          @Override
          public void changed (ChangeEvent event, Actor actor) {
            gameConnectService.changeUser(player.getUserName(), player.getCivId(), (String) botBox.getSelected());
          }
        });
        uiElementsCreator.addToTableRow(botBox, singlePlayerTable);

        var removeButton = uiElementsCreator.createDialogButton("REMOVE");
        removeButton.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            removePlayer(player.getUserName());
          }
        });
        uiElementsCreator.addToTableRow(removeButton, singlePlayerTable);
      }
      uiElementsCreator.addCellToTable(singlePlayerTable, playerList);
    }
    this.addBotButton = uiElementsCreator.createActionButton("ADD BOT", this::addBot, 0, 0);
    uiElementsCreator.addCellToTable(addBotButton, playerList);
    stage.addActor(playerTable);
  }

  private void prepareStartButton() {
    this.startButton = uiElementsCreator.createActionButton("START", this::startGame, (int) (stage.getWidth() * 0.1), (int) (stage.getHeight() * 0.05));
    uiElementsCreator.setActorWidthAndHeight(startButton, (int) (stage.getWidth() * 0.3), (int) (stage.getHeight() * 0.05));
    stage.addActor(startButton);
  }

  private void prepareExitButton() {
    this.exitButton = uiElementsCreator.createActionButton("EXIT", this::exitGame, (int) (stage.getWidth() * 0.6), (int) (stage.getHeight() * 0.05));
    uiElementsCreator.setActorWidthAndHeight(exitButton, (int) (stage.getWidth() * 0.3), (int) (stage.getHeight() * 0.05));
    stage.addActor(exitButton);
  }

  private void prepareMapSizeSelectBox() {
    this.mapSizeWindow = uiElementsCreator.createWindow("Map size");
    this.mapSizeWindow.setMovable(false);
    uiElementsCreator.setActorWidthAndHeight(mapSizeWindow, (int) (stage.getWidth() * 0.4), (int) (stage.getHeight() * 0.20));
    uiElementsCreator.setActorPosition(mapSizeWindow, (int) (stage.getWidth() * 0.05), (int) (stage.getHeight() * 0.15));

    var mapSizeSelectBox = uiElementsCreator.createSelectBox();
    uiElementsCreator.setActorWidthAndHeight(mapSizeSelectBox, (int) (stage.getWidth() * 0.25), (int) (stage.getHeight() * 0.05));
    mapSizeSelectBox.setItems(MapSize.values());
    mapSizeSelectBox.setSelected(selectedMapSize);
    mapSizeSelectBox.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        selectedMapSize = (MapSize) mapSizeSelectBox.getSelected();
        gameConnectService.changeLobby(selectedMapSize, (int) selectedMapTypeConfig.getId());
      }
    });
    mapSizeWindow.add(mapSizeSelectBox);
    stage.addActor(mapSizeWindow);
  }

  private void removePlayer(String playerName) {
    gameConnectService.removeUser(playerName);
  }

  private void prepareMapTypeSelectBox() {
    var mapTypes = gameConfigAssets.getGameConfigs().getAll(MapTypeConfig.class);

    this.mapTypeWindow = uiElementsCreator.createWindow("Map type");
    this.mapTypeWindow.setMovable(false);
    uiElementsCreator.setActorWidthAndHeight(mapTypeWindow, (int) (stage.getWidth() * 0.4), (int) (stage.getHeight() * 0.20));
    uiElementsCreator.setActorPosition(mapTypeWindow, (int) (stage.getWidth() * 0.55), (int) (stage.getHeight() * 0.15));

    var mapTypeConfigSelectBox = uiElementsCreator.createSelectBox();
    uiElementsCreator.setActorWidthAndHeight(mapTypeConfigSelectBox, (int) (stage.getWidth() * 0.25), (int) (stage.getHeight() * 0.05));
    mapTypeConfigSelectBox.setItems(mapTypes);
    mapTypeConfigSelectBox.setSelected(selectedMapTypeConfig);
    mapTypeConfigSelectBox.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        selectedMapTypeConfig = (MapTypeConfig) mapTypeConfigSelectBox.getSelected();
        gameConnectService.changeLobby(selectedMapSize, (int) selectedMapTypeConfig.getId());
      }
    });
    mapTypeWindow.add(mapTypeConfigSelectBox);
    stage.addActor(mapTypeWindow);
  }


  private void startGame() {
    gameStartService.startGame();
  }

  private void exitGame() {
    removePlayer(playerInfo.getUserName());
    game.changeToGameRoomListScreen();
  }

  private void addBot() {
    gameConnectService.addBot();
  }

  public int randomBetween(int min, int max) {
    return random.nextInt(max - min) + min;
  }

}
