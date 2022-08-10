package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.Component;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.PlayerInfo;
import com.mygdx.game.core.ecs.component.PlayerToken;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class PlayerTokenHandler implements ComponentMessageListener.Handler {

  private final PlayerInfo playerInfo;

  @Inject
  public PlayerTokenHandler(
      @NonNull PlayerInfo playerInfo
  ) {
    this.playerInfo = playerInfo;
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    log.info("Read playerToken component " + worldEntity);
    var token = (PlayerToken) component;

    if (isTokenEqualPlayerToken(token)) {
      playerInfo.activatePlayer();
    } else {
      playerInfo.deactivatePlayer();
    }

    return FULLY_HANDLED;
  }

  private boolean isTokenEqualPlayerToken(PlayerToken token) {
    return (token.getToken().equals(playerInfo.getToken()));
  }
}
