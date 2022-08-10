package com.mygdx.game.server.model;

import io.vertx.core.http.ServerWebSocket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static lombok.EqualsAndHashCode.*;

@Data
@EqualsAndHashCode
public class Client {
  @Exclude private final ServerWebSocket socket;
  @Exclude private String playerUsername;
  @Include private String secretToken;
}
