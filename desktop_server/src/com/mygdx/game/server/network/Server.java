package com.mygdx.game.server.network;

import com.mygdx.game.server.initialize.LocalMapInitializer;
import com.mygdx.game.server.initialize.LocalStartUnitInitializer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.json.Json;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public final class Server {

  private static final String HOST = "127.0.0.1";
  private static final int PORT = 10666;
  private final LocalMapInitializer mapInitializer;
  private final LocalStartUnitInitializer unitInitializer;
  private final ClientManager clientManager;
  private HttpServer server;

  @Inject
  public Server(
      LocalMapInitializer mapInitializer,
      LocalStartUnitInitializer unitInitializer,
      ClientManager clientManager) {
    this.mapInitializer = mapInitializer;
    this.unitInitializer = unitInitializer;
    this.clientManager = clientManager;
  }

  private void handle(int client, WebSocketFrame frame) {
    switch (frame.textData()) {
      case "map" -> {
        var map = mapInitializer.initializeMap();
        clientManager.getClients().values()
            .forEach(webSocket -> {
              var array = map.entrySet().stream()
                  .map(entry -> new Object[] {entry.getKey(), entry.getValue()})
                  .toArray();
              webSocket.write(Json.encodeToBuffer(array));
            });
      }
      case "unit" -> unitInitializer.initializeTestUnit(0);
      default -> log.info("Received packet: " + frame.textData());
    }
  }

  public void runServer() {
    setUpServer();
    setUpWebSocketHandler();
  }

  private void setUpServer() {
    var vertx = Vertx.vertx();
    server = vertx.createHttpServer();
  }

  private void setUpWebSocketHandler() {
    server.websocketHandler(websocket -> {
      int clientId = clientManager.addClient(websocket);
      websocket.frameHandler(frame -> this.handle(clientId, frame));
    });
    server.listen(PORT, HOST);
  }
}
