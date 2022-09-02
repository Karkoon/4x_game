package com.mygdx.game.client_core.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import io.reactivex.rxjava3.core.Completable;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
@Log
public class ServerConnection {

  private static final String HOST = "127.0.0.1"; // todo put configs in a runtime-provided object
  private static final int PORT = 10666;
  private SingleWebSocketHandler webSocketHandler;
  private final List<WebSocketListener> listeners = new ArrayList<>();

  private final WebSocketListener serverConnectionListener = new WebSocketListener() {
    @Override
    public boolean onOpen(WebSocket webSocket) {
      log.info("opened");
      return false;
    }

    @Override
    public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
      log.info("close " + closeCode + " reason " + reason);
      Gdx.app.exit();
      return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, String packet) {
      log.info("message string " + packet);
      return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, byte[] packet) {
      log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "message packet " + Arrays.toString(packet));
      return false;
    }

    @Override
    public boolean onError(WebSocket webSocket, Throwable error) {
      log.info("error");
      error.printStackTrace();
      Gdx.app.exit();
      return false;
    }
  };
  private WebSocket activeWebSocket;

  @Inject
  public ServerConnection() {
    CommonWebSockets.initiate();
  }

  public void connect() {
    activeWebSocket = WebSockets.newSocket(WebSockets.toWebSocketUrl(HOST, PORT));
    activeWebSocket.setSendGracefully(true);
    activeWebSocket.addListener(serverConnectionListener);
    activeWebSocket.connect();
    webSocketHandler = new SingleWebSocketHandler();
    activeWebSocket.addListener(webSocketHandler);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  public Completable reconnect() {
    return Completable.fromAction(() -> {
      disconnect();
      connect();
      while (!isConnected()) {
        // waiting
      }
    });
  }

  public boolean isConnected() {
    return activeWebSocket.isOpen();
  }

  public void disconnect() {
    activeWebSocket.close();
    cancelAll();
    cancel(webSocketHandler);
    activeWebSocket = null;
  }

  public Disposable addWebSocketListener(WebSocketListener webSocketListener) {
    Disposable disposable = () -> cancel(webSocketListener);
    listeners.add(webSocketListener);
    activeWebSocket.removeListener(webSocketHandler);
    activeWebSocket.addListener(webSocketListener);
    activeWebSocket.addListener(webSocketHandler);
    return disposable;
  }

  private void cancel(WebSocketListener webSocketListener) {
    activeWebSocket.removeListener(webSocketListener);
    listeners.remove(webSocketListener);
  }

  private void cancelAll() {
    listeners.forEach(this::cancel);
  }

  public void send(String text) {
    activeWebSocket.send(text);
  }

  public <T> Disposable registerSingleMessageHandler(
      Class<T> aClass,
      SingleWebSocketHandler.Handler<T> handler
  ) {
    return webSocketHandler.registerHandler(aClass, handler);
  }

}
