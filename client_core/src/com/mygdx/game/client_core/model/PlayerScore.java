package com.mygdx.game.client_core.model;

import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Getter
@Setter
@Singleton
public class PlayerScore {
  private int scoreValue = 0;

  @Inject
  public PlayerScore() {
  }
}
