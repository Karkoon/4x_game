package com.mygdx.game.server.network;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

// TODO: 04.06.2022 use it for networking??
public final class Server {

  private static final String HOST = "localhost";
  private static final int PORT = 10666;
  private HttpServer server;

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
    server.websocketHandler(webSocket -> webSocket.frameHandler(frame -> System.out.println("Received packet: " + frame.textData()))).listen(PORT, HOST);
  }
}
