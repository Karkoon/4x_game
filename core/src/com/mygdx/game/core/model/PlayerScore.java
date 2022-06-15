package com.mygdx.game.client.model;

import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Getter
@Setter
public class PlayerScore {
  private Integer scoreValue = 0;

  @Inject
  public PlayerScore() {
  }
}
