package com.mygdx.game.client_core.model;

import lombok.Data;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@Data
public class PlayerInfo {

  private final String userName;
  private final String secretToken;

  @Inject
  public PlayerInfo() {
    userName = UUID.randomUUID().toString();
    secretToken = UUID.randomUUID().toString();
  }
}
