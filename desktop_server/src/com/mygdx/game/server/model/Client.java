package com.mygdx.game.server.model;

import com.mygdx.game.core.model.PlayerLobby;
import io.vertx.core.http.ServerWebSocket;
import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Data
public class Client {

  @Exclude
  private GameRoom gameRoom;
  @Exclude
  private final ServerWebSocket socket;
  private String playerUsername;
  @Exclude
  private String playerToken;
  @Exclude
  private long civId;
  @Exclude
  private boolean isBot;

  public PlayerLobby mapToPlayerLobby() {
    return new PlayerLobby(playerUsername, civId, isBot);
  }
}
