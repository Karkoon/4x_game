package com.mygdx.game.server.network;

import com.badlogic.gdx.utils.Json;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import com.mygdx.game.server.initialize.MapInitializer;
import com.mygdx.game.server.initialize.StartUnitInitializer;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.WebSocketFrame;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public final class Server {

  private static final String HOST = "127.0.0.1";
  private static final int PORT = 10666;

  private final MapInitializer mapInitializer;
  private final StartUnitInitializer unitInitializer;
  private final MoveEntityService moveEntityService;
  private final GameRoom room;


  private final Json json = new Json();
  private HttpServer server;

  @Inject
  public Server(
      MapInitializer mapInitializer,
      StartUnitInitializer unitInitializer,
      MoveEntityService moveEntityService,
      GameRoom room
  ) {
    this.mapInitializer = mapInitializer;
    this.unitInitializer = unitInitializer;
    this.moveEntityService = moveEntityService;
    this.room = room;
  }

  private void handle(
      @NonNull Client client,
      @NonNull WebSocketFrame frame
  ) {
    var commands = frame.textData().split(":");
    var type = commands[0];
    log.info("Received frame: " + frame.textData() + " from " + client + " clients" + room.getNumberOfClients());
    switch (type) {
      case "connect" -> { // TODO: 16.06.2022 connect to specific room
        room.getClients().forEach(ws -> {
          var msg = new PlayerJoinedRoomMessage(room.getNumberOfClients());
          var buffer = Buffer.buffer(json.toJson(msg, (Class<?>) null));
          ws.getSocket().write(buffer);
        });
      }
      case "start" -> {
        var width = Integer.parseInt(commands[1]);
        var height = Integer.parseInt(commands[2]);
        mapInitializer.initializeMap(width, height, client);
        unitInitializer.initializeTestUnit(client);
        room.getClients().forEach(ws -> {
          var msg = new GameStartedMessage();
          var buffer = Buffer.buffer(json.toJson(msg, (Class<?>) null));
          ws.getSocket().write(buffer);
        });
      }
      case "move" -> {
        var entityId = Integer.parseInt(commands[1]);
        var x = Integer.parseInt(commands[2]);
        var y = Integer.parseInt(commands[3]);
        moveEntityService.moveEntity(entityId, x, y);
      }
      default -> log.info("Couldn't handle packet: " + frame.textData());
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
      var client = new Client(websocket);
      room.addClient(client);
      websocket.frameHandler(frame -> this.handle(client, frame));
    });
    server.listen(PORT, HOST);
  }
}
