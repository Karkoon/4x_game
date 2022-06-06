package com.mygdx.game.server.network;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import lombok.extern.java.Log;

import javax.inject.Inject;

// TODO: 04.06.2022 use it for networking??
@Log
public final class Server {

  private static final String HOST = "localhost";
  private static final int PORT = 10666;
  private HttpServer server;

  @Inject
  public Server() {
  }

  public void runServer() {
    setUpServer();
    setUpWebSocketHandler();
  }

  private void setUpServer() {
    Vertx vertx = Vertx.vertx();
    server = vertx.createHttpServer();
  }

  private void setUpWebSocketHandler() {
    server.websocketHandler(webSocket -> webSocket.frameHandler(frame -> log.info("Received packet: " + frame))).listen(PORT, HOST);
  }
}
