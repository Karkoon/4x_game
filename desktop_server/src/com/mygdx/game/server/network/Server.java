package com.mygdx.game.server.network;

import com.mygdx.game.server.initialize.LocalMapInitializer;
import com.mygdx.game.server.initialize.LocalStartUnitInitializer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.json.Json;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.stream.Stream;

@Log
public final class Server {

  private static final String HOST = "127.0.0.1";
  private static final int PORT = 10666;
  private final LocalMapInitializer mapInitializer;
  private final LocalStartUnitInitializer unitInitializer;
  private final ClientManager clientManager;
  private HttpServer server;
  private MoveEntityService moveEntityService;

  @Inject
  public Server(
      LocalMapInitializer mapInitializer,
      LocalStartUnitInitializer unitInitializer,
      ClientManager clientManager,
      MoveEntityService moveEntityService) {
    this.mapInitializer = mapInitializer;
    this.unitInitializer = unitInitializer;
    this.clientManager = clientManager;
    this.moveEntityService = moveEntityService;
  }

  private void handle(int client, WebSocketFrame frame) {
    Object[] commands = Arrays.stream(frame.textData().split(":")).toArray();
    String type = (String) commands[0];
    switch (type) {
      case "map" -> {
        var map = mapInitializer.initializeMap();
        clientManager.getClients().values()
          .forEach(webSocket -> {
            var array = map.entrySet().stream()
                .map(entry -> new Object[] {entry.getKey(), entry.getValue()})
                .toArray();
            var header = Arrays.stream(new Object[] {Arrays.stream(new Object[] {"map", "map"}).toArray()}).toArray();
            var mapArray = Stream.concat(Arrays.stream(header), Arrays.stream(array)).toArray();
            webSocket.write(Json.encodeToBuffer(mapArray));
          });
      }
      case "unit" -> {
        var map = unitInitializer.initializeTestUnit(0);
        clientManager.getClients().values()
          .forEach(webSocket -> {
            var array = map.entrySet().stream()
                    .map(entry -> new Object[] {entry.getKey(), entry.getValue()})
                    .toArray();
            var header = Arrays.stream(new Object[] {Arrays.stream(new Object[] {"unit", "unit"}).toArray()}).toArray();
            var mapArray = Stream.concat(Arrays.stream(header), Arrays.stream(array)).toArray();
            webSocket.write(Json.encodeToBuffer(mapArray));
          });
      }
      case "move" -> {
        var componentMessagePosition = moveEntityService.moveEntity((String) commands[1], (String) commands[2], (String) commands[3]);

        clientManager.getClients().values()
                .forEach(webSocket -> {
                  System.out.println("Send position component");
                  webSocket.write(Json.encodeToBuffer(componentMessagePosition));
                });
      }
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
