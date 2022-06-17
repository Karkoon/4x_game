package com.mygdx.game.server.network;

import com.badlogic.gdx.utils.Json;
import com.mygdx.game.server.initialize.MapInitializer;
import com.mygdx.game.server.initialize.StartUnitInitializer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.WebSocketFrame;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public final class Server {

  private static final String HOST = "127.0.0.1";
  private static final int PORT = 10666;
  private final MapInitializer mapInitializer;
  private final StartUnitInitializer unitInitializer;
  private final ClientManager clientManager;
  private final MoveEntityService moveEntityService;
  private final ComponentSyncer syncer;
  private final Json json = new Json();
  private HttpServer server;

  @Inject
  public Server(
      MapInitializer mapInitializer,
      StartUnitInitializer unitInitializer,
      ClientManager clientManager,
      MoveEntityService moveEntityService,
      ComponentSyncer syncer) {
    this.mapInitializer = mapInitializer;
    this.unitInitializer = unitInitializer;
    this.clientManager = clientManager;
    this.moveEntityService = moveEntityService;
    this.syncer = syncer;
  }

  private void handle(int client, WebSocketFrame frame) {
    var commands = frame.textData().split(":");
    var type = commands[0];
    log.info("Received frame: " + frame.textData() + " from " + client);
    switch (type) {
      case "map" -> mapInitializer.initializeMap(client);
      case "unit" -> unitInitializer.initializeTestUnit(client);
      case "move" -> {
        var entityId = Integer.parseInt(commands[1]);
        var x = Integer.parseInt(commands[2]);
        var y = Integer.parseInt(commands[3]);
        moveEntityService.moveEntity(entityId, x, y);
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
