package com.mygdx.game.server.model;

import io.vertx.core.http.ServerWebSocket;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Data
public class Client {
  @Exclude
  private GameRoom gameRoom;
  @Exclude
  private final ServerWebSocket socket;
  @Exclude
  private String playerUsername;
  @Exclude
  private String playerToken;
}
