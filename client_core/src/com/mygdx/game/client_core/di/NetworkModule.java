package com.mygdx.game.client_core.di;

import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSockets;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.message_handlers.ChangeTurnMessageHandler;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Singleton;

@Module
@Log
public class NetworkModule {

  private static final String HOST = "127.0.0.1"; // todo put configs in a runtime-provided object
  private static final int PORT = 10666;

  @Provides
  @Singleton
  public WebSocket providesWebsocket() {
    CommonWebSockets.initiate();
    var socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(HOST, PORT));
    socket.setSendGracefully(true);
    log.info("provided socket: " + socket);
    socket.connect();
    while (!socket.isOpen()) {
      /* wait till connection is ready, later this code should be redone */
    }
    return socket;
  }

  @Provides
  public WebSocketHandler providesWebSocketHandler(
      @NonNull WebSocket webSocket
  ) {
    var handler = new WebSocketHandler();
    handler.setFailIfNoHandler(false);
    webSocket.addListener(handler);
    return handler;
  }
}
