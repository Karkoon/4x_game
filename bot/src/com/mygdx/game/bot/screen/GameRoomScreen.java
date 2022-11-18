package com.mygdx.game.bot.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.github.czyzby.websocket.WebSocket;

import com.mygdx.game.bot.GdxGame;
import com.mygdx.game.bot.di.gameinstance.GameScreenSubcomponent;
import com.mygdx.game.bot.model.ChosenBotType;
import com.mygdx.game.client_core.model.NetworkJobsQueueJobJobberManager;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.model.PlayerLobby;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import com.mygdx.game.core.network.messages.RoomConfigMessage;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@Singleton
public class GameRoomScreen extends ScreenAdapter {

  private final ChosenBotType chosenBotType;
  private final QueueMessageListener listener;
  private final GameScreenSubcomponent.Builder gameScreenBuilder;
  private final PlayerInfo playerInfo;
  private final NetworkJobsQueueJobJobberManager jobManager;
  private final GdxGame game;

  @Inject
  GameRoomScreen(
      ChosenBotType chosenBotType,
      QueueMessageListener listener,
      @NonNull GameScreenSubcomponent.Builder gameScreenBuilder,
      PlayerInfo playerInfo,
      NetworkJobsQueueJobJobberManager jobManager,
      GdxGame game
  ) {
    this.chosenBotType = chosenBotType;
    this.listener = listener;
    this.gameScreenBuilder = gameScreenBuilder;
    this.jobManager = jobManager;
    this.game = game;
    this.playerInfo = playerInfo;
  }

  @Override
  public void show() {
    if (!initialized) {
      listener.registerHandler(GameStartedMessage.class, (webSocket, message) -> {
            var gameScreen = gameScreenBuilder.build().get();
            gameScreen.setActivePlayerToken(message.getPlayerToken());
            game.setScreen(gameScreen);
            return FULLY_HANDLED;
          }
      );
      listener.registerHandler(PlayerJoinedRoomMessage.class, this::changeType);
      listener.registerHandler(RoomConfigMessage.class, this::noOp2);
      initialized = true;
    }
  }
  private boolean initialized = false;

  @Override
  public void render(float delta) {
    jobManager.doAllJobs();
  }

  private boolean changeType(WebSocket webSocket, PlayerJoinedRoomMessage message) {
    for (PlayerLobby user : message.getUsers()) {
      if (user.getUserName().equals(playerInfo.getUserName())) {
        chosenBotType.setBotType(user.getBotType());
      }
    }
    return true;
  }
  private boolean noOp2(WebSocket webSocket, RoomConfigMessage message) {
    return true;
  }
}
