package com.mygdx.game.server.model;

import io.vertx.core.http.ServerWebSocket;
import lombok.Data;

@Data
public class Client {
  private int id;
  private final ServerWebSocket socket;
}
