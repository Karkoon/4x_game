package com.mygdx.game.client.network;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import dagger.Reusable;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@Reusable
// not used
public class GameCommunicationHandler extends WebSocketHandler {

  @Inject
  public GameCommunicationHandler() {
    // todo think about injecting deserialization handlers into the constructor, or through a Dagger
    //  module provider
    // (not sure what is the right approach in this case)
    // if we didn't have overriden onOpen and onClose then I wouldn't leave it as a separate class and
    // and would inject the deserialization handlers into the provider, by making for each of them a separate
    // class file. Or I would try to generify and make every message be deserialized. There's a problem with
    // knowing the types though.
    // It could be tried to make a generic json deserializer which would be instantiated with Class<T> and <T>
    // from a factory method/class and reduce the number of codelines.
    // I don't think these objects would be worth injecting so it could be done in this constructor.
    // .
    // .
    // .
    // Or the deserialization handler factory could be injected and it we would create a Gson deserializer factory
    // or any other deserialization library. Even protobuff.
    // At least I think so.
  }

  @Override
  public boolean onOpen(final WebSocket webSocket) {
    log.info("Successfully connected to server");
    webSocket.send("Hello from client!");
    return FULLY_HANDLED;
  }

  @Override
  public boolean onClose(final WebSocket webSocket, final int code, final String reason) {
    log.info("Disconnected - status: " + code + ", reason: " + reason);
    return FULLY_HANDLED;
  }

  @Override
  public boolean onMessage(final WebSocket webSocket, final String packet) {
    log.info("Got message: " + packet);
    return super.onMessage(webSocket, packet);
  }

  @Override
  public boolean onError(WebSocket webSocket, Throwable error) {
    log.info("Error");
    return super.onError(webSocket, error);
  }
}


