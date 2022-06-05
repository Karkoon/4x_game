package com.mygdx.game;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class ServerLauncher {
  public static void main(final String... args) {
    System.out.println("Launching web socket server...");
    final Vertx vertx = Vertx.vertx();
    final HttpServer server = vertx.createHttpServer();
    server.websocketHandler(webSocket -> {
      // Printing received packets to console:
      webSocket.frameHandler(frame -> System.out.println("Received packet: " + frame.textData()));
      // Sending a simple message:
      webSocket.writeFinalTextFrame("Hello from server!");
      // Closing the socket in 5 seconds:
      vertx.setTimer(5000L, id -> webSocket.close());
    }).listen(8001);
    System.out.println("Waiting");
  }
}
