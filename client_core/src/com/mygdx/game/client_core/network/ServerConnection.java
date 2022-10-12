package com.mygdx.game.client_core.network;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.mygdx.game.client_core.model.NetworkJobsQueueJobJobberManager;
import com.mygdx.game.client_core.model.ServerConnectionConfig;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
@Log
public class ServerConnection {
  private final ConcurrentLinkedQueue<String> dataToSend = new ConcurrentLinkedQueue<>();

  private WebSocketListener persistentListener;
  private final WebSocketListener serverConnectionListener = new WebSocketListener() {
    @Override
    public boolean onOpen(WebSocket webSocket) {
      log.info("opened server connection");
      return false;
    }

    @Override
    public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
      log.info("closed server connection, code: " + closeCode + " reason " + reason);
      Gdx.app.exit();
      return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, String packet) {
      log.info("message string " + packet);
      return persistentListener.onMessage(webSocket, packet);
    }

    @Override
    public boolean onMessage(WebSocket webSocket, byte[] packet) {
      log.info("message packet " + Arrays.toString(packet));
      return persistentListener.onMessage(webSocket, packet);
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

  public void connect(ServerConnectionConfig config) {
    activeWebSocket = WebSockets.newSocket(WebSockets.toWebSocketUrl(config.getHost(), config.getPort()));
    activeWebSocket.setSendGracefully(true);
    activeWebSocket.addListener(serverConnectionListener);
    activeWebSocket.connect();
    while (!activeWebSocket.isOpen()) {
      /* wait till connection is ready */
    }
    activeWebSocket.addListener(serverConnectionListener);
  }

  public void reconnect(ServerConnectionConfig config) {
    disconnect();
    connect(config);
  }

  public boolean isConnected() {
    return activeWebSocket.isOpen();
  }

  public void disconnect() {
    activeWebSocket.close();
    activeWebSocket = null;
  }

  public void send(String text) {
    if (activeWebSocket.isOpen()) {
      var iter = dataToSend.iterator();
      while (iter.hasNext()) {
        if (activeWebSocket.isOpen()) {
          activeWebSocket.send(iter.next());
        }
      }
      activeWebSocket.send(text);
    } else {
      dataToSend.add(text);
    }
  }

  public void setPersistentListener(WebSocketListener messageListener) {
    persistentListener = messageListener;
  }
}
