package com.mygdx.game.client.di;

import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.mygdx.game.client.network.ComponentHandler;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public class NetworkModule {

  private static final String HOST = "localhost";
  private static final int PORT = 10666;

  static {
    CommonWebSockets.initiate();
  }

  @Provides
  @Reusable
  public WebSocket providesWebsocket(WebSocketListener listener) {
    var socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(HOST, PORT));
    socket.setSendGracefully(true);
    socket.addListener(listener);
    return socket;
  }

  @Provides
  @Reusable
  public WebSocketListener providesWebSocketListener(ComponentHandler handler) {
    return handler;
  }
}
