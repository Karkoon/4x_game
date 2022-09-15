package com.mygdx.game.server.network;

import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.handlers.CloseHandler;
import com.mygdx.game.server.network.handlers.ConnectHandler;
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

  private final StartHandler startHandler;
  private final ConnectHandler connectHandler;
  private final CloseHandler closeHandler;

  private HttpServer server;

  @Inject
  public Server(
      StartHandler startHandler,
      ConnectHandler connectHandler,
      CloseHandler closeHandler
  ) {
    this.startHandler = startHandler;
    this.connectHandler = connectHandler;
    this.closeHandler = closeHandler;
  }

  private void handle(
      @NonNull Client client,
      @NonNull WebSocketFrame frame
  ) {
    var commands = frame.textData().split(":");
    var type = commands[0];
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "Received frame: " + frame.textData() + " from " + client.getPlayerUsername());
    switch (type) {
      case "start" -> startHandler.handle(commands, client);
      case "connect" -> connectHandler.handle(commands, client);
      default -> client.getGameRoom().getGameInstance().getServer().handle(commands, client);
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
      websocket
          .frameHandler(frame -> this.handle(client, frame))
          .closeHandler(event -> {
            log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "the client closed? " + client.getPlayerUsername());
            closeHandler.handle(client);
          }).endHandler(event -> {
            log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "the client ended? " + client.getPlayerUsername());
            // TODO: 13.08.2022 cleanup after client
          }).exceptionHandler(throwable -> {
            log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "the client threw up? " + client.getPlayerUsername());
            throwable.printStackTrace();
            // TODO: 13.08.2022 cleanup after client
          });
    });
    server.listen(PORT, HOST);
  }
}
