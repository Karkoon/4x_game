package com.mygdx.game.client_core.di;

import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.Arrays;

@Module
@Log
public class NetworkModule {

  private static final String HOST = "127.0.0.1"; // todo put configs in a runtime-provided object
  private static final int PORT = 10666;

  @Provides
  @Singleton
  public WebSocket providesWebsocket() {// todo wrap WebSocket into an object that could reconnected when needed
    CommonWebSockets.initiate();
    var socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(HOST, PORT));
    socket.setSendGracefully(true);
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "provided socket: " + socket);
    socket.addListener(new WebSocketListener() {
      @Override
      public boolean onOpen(WebSocket webSocket) {
        log.info("opened");
        return false;
      }

      @Override
      public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
        log.info("close " + closeCode + " reason " + reason);
        return false;
      }

      @Override
      public boolean onMessage(WebSocket webSocket, String packet) {
        log.info("message string " + packet);
        return false;
      }

      @Override
      public boolean onMessage(WebSocket webSocket, byte[] packet) {
        log.info("message packet " + Arrays.toString(packet));
        return false;
      }

      @Override
      public boolean onError(WebSocket webSocket, Throwable error) {
        log.info("error");
        return false;
      }
    });
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
    log.info("provides handler websocket " + webSocket);
    var handler = new WebSocketHandler();
    handler.setFailIfNoHandler(false);
    webSocket.addListener(handler);
    return handler;
  }
}
