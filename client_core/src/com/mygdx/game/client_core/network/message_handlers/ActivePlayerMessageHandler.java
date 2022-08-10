package com.mygdx.game.client_core.network.message_handlers;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.client_core.model.ActivePlayerInfo;
import com.mygdx.game.core.network.messages.ActivePlayerMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class ActivePlayerMessageHandler implements WebSocketHandler.Handler<ActivePlayerMessage> {

  private final ActivePlayerInfo activePlayerInfo;

  @Inject
  public ActivePlayerMessageHandler(
      ActivePlayerInfo activePlayerInfo
  ) {
    this.activePlayerInfo = activePlayerInfo;
  }

  @Override
  public boolean handle(WebSocket webSocket, ActivePlayerMessage activePlayerMessage) {
    log.info("Handle active player message " + activePlayerMessage);
    activePlayerInfo.setUsername(activePlayerMessage.getCurrentPlayerUsername());
    return FULLY_HANDLED;
  }
}
