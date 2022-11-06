package com.mygdx.game.bot.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.github.czyzby.websocket.WebSocket;

import com.google.inject.Singleton;
import com.mygdx.game.bot.GdxGame;
import com.mygdx.game.bot.di.gameinstance.GameScreenSubcomponent;
import com.mygdx.game.client_core.model.NetworkJobsQueueJobJobberManager;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import lombok.NonNull;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Singleton
public class GameRoomScreen extends ScreenAdapter {

  private final QueueMessageListener listener;
  private final GameScreenSubcomponent.Builder gameScreenBuilder;
  private final NetworkJobsQueueJobJobberManager jobManager;
  private final GdxGame game;

  @Inject
  GameRoomScreen(
      QueueMessageListener listener,
      @NonNull GameScreenSubcomponent.Builder gameScreenBuilder,
      NetworkJobsQueueJobJobberManager jobManager,
      GdxGame game
  ) {
    this.listener = listener;
    this.gameScreenBuilder = gameScreenBuilder;
    this.jobManager = jobManager;
    this.game = game;
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
      listener.registerHandler(PlayerJoinedRoomMessage.class, this::noOp);
      initialized = true;
    }
  }
  private boolean initialized = false;

  @Override
  public void render(float delta) {
    jobManager.doAllJobs();
  }

  private boolean noOp(WebSocket webSocket, PlayerJoinedRoomMessage message) {
    return true;
  }
}
