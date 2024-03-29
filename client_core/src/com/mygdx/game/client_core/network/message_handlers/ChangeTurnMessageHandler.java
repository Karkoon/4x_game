package com.mygdx.game.client_core.network.message_handlers;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@GameInstanceScope
@Log
public class ChangeTurnMessageHandler implements QueueMessageListener.Handler<ChangeTurnMessage> {

  private final ActiveToken activeToken;

  @Inject
  public ChangeTurnMessageHandler(
      ActiveToken activeToken
  ) {
    this.activeToken = activeToken;
  }

  @Override
  public boolean handle(WebSocket webSocket, ChangeTurnMessage message) {
    log.info("Handle change turn message ");
    activeToken.setActiveToken(message.getPlayerToken());
    return FULLY_HANDLED;
  }

}
