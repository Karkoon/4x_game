package com.mygdx.game.client_core.network.message_handlers;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class ChangeTurnMessageHandler implements WebSocketHandler.Handler<ChangeTurnMessage> {

  private final ActiveToken activeToken;

  @Inject
  public ChangeTurnMessageHandler(
      @NonNull ActiveToken activeToken
  ) {
    this.activeToken = activeToken;
  }

  @Override
  public boolean handle(WebSocket webSocket, ChangeTurnMessage changeTurnMessage) {
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "Handle change turn message ");
    activeToken.setActiveToken(changeTurnMessage.getPlayerToken());
    return FULLY_HANDLED;
  }

}
