package com.mygdx.game.client_core.network.message_handlers;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class ChangeTurnMessageHandler implements WebSocketHandler.Handler<ChangeTurnMessage> {

  private final PlayerInfo playerInfo;

  @Inject
  public ChangeTurnMessageHandler(
      PlayerInfo playerInfo
  ) {
    this.playerInfo = playerInfo;
  }

  @Override
  public boolean handle(WebSocket webSocket, ChangeTurnMessage changeTurnMessage) {
    log.info("Handle change turn message ");
    if (changeTurnMessage.getPlayerToken().equals(playerInfo.getToken())) {
      playerInfo.activatePlayer();
    } else {
      playerInfo.deactivatePlayer();
    }
    return FULLY_HANDLED;
  }

}
