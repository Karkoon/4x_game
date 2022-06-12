package com.mygdx.game.client.di;

import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSockets;
import com.mygdx.game.client.network.ComponentMessageListener;
import com.mygdx.game.client.network.GameStateListener;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

import javax.inject.Singleton;

@Module
@Log
public class NetworkModule {

  private static final String HOST = "127.0.0.1";
  private static final int PORT = 10666;

  @Provides
  @Singleton
  public WebSocket providesWebsocket(
      GameStateListener gameStateListener,
      WebSocketHandler handler,
      ComponentMessageListener messageListener
  ) {
    CommonWebSockets.initiate();
    var socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(HOST, PORT));
    socket.setSendGracefully(true);
    socket.addListener(gameStateListener);
    socket.addListener(handler);
    socket.addListener(messageListener);
    log.info("provided socket: " + socket);
    socket.connect();
    while (!socket.isOpen()) {
      /* wait till connection is ready, later this code should be redone */
    }
    return socket;
  }

  @Provides
  @Singleton
  public WebSocketHandler providesWebSocketHandler() {
    return new WebSocketHandler();
  }
}
