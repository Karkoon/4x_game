package com.mygdx.game.bot.network.comp_handlers;

import com.artemis.Component;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.model.GameState;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import lombok.NonNull;

import javax.inject.Inject;

public class SaveToMapHandler implements ComponentMessageListener.Handler {

  private final GameState gameState;

  @Inject
  public SaveToMapHandler(
      @NonNull GameState gameState
  ) {
    this.gameState = gameState;
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    gameState.saveEntity(worldEntity);
    return true;
  }
}
