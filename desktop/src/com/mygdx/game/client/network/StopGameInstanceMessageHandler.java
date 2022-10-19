package com.mygdx.game.client.network;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.network.messages.StopGameInstanceMessage;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class StopGameInstanceMessageHandler implements QueueMessageListener.Handler<StopGameInstanceMessage> {

  private final Lazy<Navigator> navigator;

  @Inject
  public StopGameInstanceMessageHandler(
      Lazy<Navigator> navigator
  ) {
    this.navigator = navigator;
  }

  @Override
  public boolean handle(WebSocket webSocket, StopGameInstanceMessage message) {
    log.info("Handle player exited message ");
    navigator.get().exit();
    return FULLY_HANDLED;
  }

}
