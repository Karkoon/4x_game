package com.mygdx.game.client.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
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

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class GameRoomScreenHUD implements Disposable {

  private final GameConnectService connectService;
  private final GameConfigAssets gameConfigAssets;
  private final GameStartService gameStartService;
  private final GameScreenSubcomponent.Builder gameScreenBuilder;
  private final GdxGame game;
  private final Stage stage;
  private final Viewport viewport;
  private final PlayerInfo playerInfo;
  private final UiElementsCreator uiElementsCreator;
  private final QueueMessageListener queueMessageListener;
  private final PlayerAlreadyInTheRoomDialogFactory playerAlreadyInTheRoomDialogFactory;

  private List<PlayerLobby> players;
  private MapSize selectedMapSize;
  private MapTypeConfig selectedMapTypeConfig;


  private Table playerTable;
  private SelectBox mapSizeSelectBox;
  private SelectBox mapTypeConfigSelectBox;
  private Button startButton;
  private Button addBotButton;

  @Inject
  public GameRoomScreenHUD(
      GameConnectService connectService,
      GameConfigAssets gameConfigAssets,
      GameStartService gameStartService,
      GameScreenSubcomponent.Builder gameScreenBuilder,
      GdxGame game,
      @Named(StageModule.SCREEN_STAGE) Stage stage,
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
    this.uiElementsCreator = uiElementsCreator;
    this.queueMessageListener = queueMessageListener;
    this.playerAlreadyInTheRoomDialogFactory = playerAlreadyInTheRoomDialogFactory;

    this.players = new ArrayList<>();
    this.selectedMapSize = MapSize.VERY_SMALL;
    this.selectedMapTypeConfig = gameConfigAssets.getGameConfigs().get(MapTypeConfig.class, GameConfigs.MAP_TYPE_MIN);
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

    Gdx.input.setInputProcessor(stage);
  }

  private void preparePlayerTable() {
    float width = stage.getWidth();
    float height = stage.getHeight();
    this.playerTable = uiElementsCreator.createTable((float) (width * 0.05), (float) (height * 0.05));
    uiElementsCreator.setActorWidthAndHeight(this.playerTable, (int) (width * 0.5), (int) (height * 0.85));
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
          playerInfo.setCivilization(((CivilizationConfig) selectBox.getSelected()).getId());
          connectService.changeUser();
        }
      });
      if (!player.getUserName().equals(playerInfo.getUserName()))
        selectBox.setDisabled(true);
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
            connectService.changeUser(player.getUserName(), (String) botBox.getSelected());
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
      uiElementsCreator.addCellToTable(singlePlayerTable, playerTable);
    }
    this.addBotButton = uiElementsCreator.createActionButton("ADD BOT", this::addBot, 0, 0);
    uiElementsCreator.addCellToTable(addBotButton, playerTable);
    stage.addActor(playerTable);
  }

  private void prepareStartButton() {
    float width = stage.getWidth();
    float height = stage.getHeight();
    this.startButton = uiElementsCreator.createActionButton("START", this::startGame, (int) (width * 0.7), (int) (height * 0.1));
    uiElementsCreator.setActorWidthAndHeight(startButton, (int) (width * 0.25), (int) (height * 0.05));
    stage.addActor(startButton);
  }

  private void prepareMapSizeSelectBox() {
    float width = stage.getWidth();
    float height = stage.getHeight();
    this.mapSizeSelectBox = uiElementsCreator.createSelectBox();
    uiElementsCreator.setActorPosition(mapSizeSelectBox, (int) (width * 0.7), (int) (height * 0.2));
    uiElementsCreator.setActorWidthAndHeight(mapSizeSelectBox, (int) (width * 0.25), (int) (height * 0.05));
    this.mapSizeSelectBox.setItems(MapSize.values());
    this.mapSizeSelectBox.setSelected(selectedMapSize);
    this.mapSizeSelectBox.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        selectedMapSize = (MapSize) mapSizeSelectBox.getSelected();
        connectService.changeLobby(selectedMapSize, (int) selectedMapTypeConfig.getId());
      }
    });
    stage.addActor(mapSizeSelectBox);
  }

  private void removePlayer(String playerName) {
    connectService.removeUser(playerName);
  }

  private void prepareMapTypeSelectBox() {
    float width = stage.getWidth();
    float height = stage.getHeight();
    var mapTypes = gameConfigAssets.getGameConfigs().getAll(MapTypeConfig.class);
    this.mapTypeConfigSelectBox = uiElementsCreator.createSelectBox();
    uiElementsCreator.setActorPosition(mapTypeConfigSelectBox, (int) (width * 0.7), (int) (height * 0.3));
    uiElementsCreator.setActorWidthAndHeight(mapTypeConfigSelectBox, (int) (width * 0.25), (int) (height * 0.05));
    this.mapTypeConfigSelectBox.setItems(mapTypes);
    this.mapTypeConfigSelectBox.setSelected(selectedMapTypeConfig);
    this.mapTypeConfigSelectBox.addListener(new ChangeListener() {
      @Override
      public void changed (ChangeEvent event, Actor actor) {
        selectedMapTypeConfig = (MapTypeConfig) mapTypeConfigSelectBox.getSelected();
        connectService.changeLobby(selectedMapSize, (int) selectedMapTypeConfig.getId());
      }
    });
    stage.addActor(mapTypeConfigSelectBox);
  }

  private void startGame() {
    gameStartService.startGame();
  }

  private void addBot() {
    connectService.addBot();
  }

}
