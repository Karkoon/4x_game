package com.mygdx.game.client_core.network;

import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@Getter
public class PlayerInfo {

  private final String userName;
  private final String token;
  private boolean playerTurn;

  @Inject
  public PlayerInfo() {
    userName = UUID.randomUUID().toString();
    token = UUID.randomUUID().toString();
    playerTurn = false;
  }

  public void activatePlayer() {
    playerTurn = true;
  }

  public void deactivatePlayer() {
    playerTurn = false;
  }

}
