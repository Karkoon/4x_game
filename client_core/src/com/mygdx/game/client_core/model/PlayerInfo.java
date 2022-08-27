package com.mygdx.game.client_core.model;

import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@Getter
public class PlayerInfo {

  private final String userName;
  private final String token;

  @Inject
  public PlayerInfo() {
    userName = UUID.randomUUID().toString();
    token = UUID.randomUUID().toString();
  }

}
