package com.mygdx.game.client_core.model;

import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Getter
@Singleton
public class PlayerInfo {

  private final String userName;
  private final String token;

  @Inject
  public PlayerInfo() { // TODO: 10.10.2022 make username configurable
    userName = UUID.randomUUID().toString();
    token = UUID.randomUUID().toString();
  }
}
