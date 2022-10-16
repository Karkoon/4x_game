package com.mygdx.game.client_core.network.message_handlers;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;


@Log
@Singleton
public class GameStartedMessageHandler implements QueueMessageListener.Handler<GameStartedMessage> {

  private final ActiveToken activeToken;

  @Inject
  public GameStartedMessageHandler(
      ActiveToken activeToken
  ) {
    this.activeToken = activeToken;
  }

  @Override
  public boolean handle(WebSocket webSocket, GameStartedMessage message) {
    log.info("game started handler token: " + message.getPlayerToken());
    activeToken.setActiveToken(message.getPlayerToken());
    return true;
  }
}
