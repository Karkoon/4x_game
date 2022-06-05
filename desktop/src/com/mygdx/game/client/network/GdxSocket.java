package com.mygdx.game.client.network;

import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketAdapter;
import com.github.czyzby.websocket.WebSockets;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class GdxSocket {

  private static final String HOST = "localhost";
  private static final int PORT = 10666;

  private WebSocket socket;

  public void initializeSocket() {
    CommonWebSockets.initiate();

    socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(HOST, PORT));
    socket.setSendGracefully(true);
    socket.addListener(getListener());
    System.out.println("Connecting");
    socket.connect();
  }

  private static WebSocketAdapter getListener() {
    return new WebSocketAdapter() {
      @Override
      public boolean onOpen(final WebSocket webSocket) {
        log.log(Level.INFO, "Successfully connected to server");
        webSocket.send("Hello from client!");
        return FULLY_HANDLED;
      }

      @Override
      public boolean onClose(final WebSocket webSocket, final int code, final String reason) {
        log.log(Level.INFO, "Disconnected - status: " + code + ", reason: " + reason);
        return FULLY_HANDLED;
      }

      @Override
      public boolean onMessage(final WebSocket webSocket, final String packet) {
        log.log(Level.INFO, "Got message: " + packet);
        return FULLY_HANDLED;
      }

      @Override
      public boolean onError(WebSocket webSocket, Throwable error) {
        log.log(Level.INFO, "Error");
        return super.onError(webSocket, error);
      }
    };
  }
}
