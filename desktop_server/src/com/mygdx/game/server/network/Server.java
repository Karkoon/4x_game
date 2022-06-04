package com.mygdx.game.server.network;

// TODO: 04.06.2022 use it for networking??
public final class Server {

/*  private static final String HOST = "127.0.0.1";
  private static final int PORT = 10666;
  private final Engine engine;
  private WebSocket socket;

  public Server(@NonNull final Engine engine) {
    this.engine = engine;
    setUpSocket();
  }

  public void connect() {
    socket.connect();
  }

  private void setUpSocket() {
    socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(HOST, PORT));
    socket.setSendGracefully(true);
    var listener = setUpWebSocketListener();
    socket.addListener(listener);
  }

  private WebSocketListener setUpWebSocketListener() {
    var listener = new WebSocketHandler();
    listener.setFailIfNoHandler(true);
    listener.registerHandler(UnitMovement.class, new TransformationHandler(engine));
    return listener;
  }*/
}
