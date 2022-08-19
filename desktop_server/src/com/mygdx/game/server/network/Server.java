package com.mygdx.game.server.network;

import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.handlers.ConnectHandler;
import com.mygdx.game.server.network.handlers.EndTurnHandler;
import com.mygdx.game.server.network.handlers.MoveHandler;
import com.mygdx.game.server.network.handlers.StartHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.WebSocketFrame;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public final class Server {

  private static final String HOST = "127.0.0.1";
  private static final int PORT = 10666;

  private final GameRoom room;
  private final StartHandler startHandler;
  private final MoveHandler moveHandler;
  private final EndTurnHandler endTurnHandler;
  private final ConnectHandler connectHandler;

  private HttpServer server;

  @Inject
  public Server(
      GameRoom room,
      StartHandler startHandler,
      MoveHandler moveHandler,
      EndTurnHandler endTurnHandler,
      ConnectHandler connectHandler
  ) {
    this.room = room;
    this.startHandler = startHandler;
    this.moveHandler = moveHandler;
    this.endTurnHandler = endTurnHandler;
    this.connectHandler = connectHandler;
  }

  private void handle(
      @NonNull Client client,
      @NonNull WebSocketFrame frame
  ) {
    var commands = frame.textData().split(":");
    var type = commands[0];
    log.info("Received frame: " + frame.textData() + " from " + client + " clients" + room.getNumberOfClients());
    switch (type) {
      case "connect" -> connectHandler.handle(commands, client);
      case "start" -> startHandler.handle(commands);
      case "move" -> moveHandler.handle(commands);
      case "end_turn" -> endTurnHandler.handle(client);
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
