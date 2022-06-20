package com.mygdx.game.client.di.module;

import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSockets;
import com.mygdx.game.client.di.scope.GameScreenScope;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

@Module()
@Log
public class NetworkModule {

  private final String host;
  private final int port;


  public NetworkModule(String host, int port) {
    this.host = host;
    this.port = port;
  }

  @Provides
  @GameScreenScope
  public WebSocket providesWebsocket() {
    CommonWebSockets.initiate();
    var socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(host, port));
    socket.setSendGracefully(true);
    log.info("provided socket: " + socket);

    socket.connect();
    while (!socket.isOpen()) {
      /* wait till connection is ready, later this code should be redone */
    }
    log.info("socket connected");
    return socket;
  }

  @Provides
  @GameScreenScope
  public WebSocketHandler providesWebSocketHandler(
      WebSocket webSocket
  ) {
    var handler = new WebSocketHandler();
    handler.setFailIfNoHandler(false);
    webSocket.addListener(handler);
    return handler;
  }
}
