package com.mygdx.game.client_core.model;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

@GameInstanceScope
@Getter
@Setter
public class PlayerScore {

  private int scoreValue = 0;

  @Inject
  public PlayerScore() {
    super();
  }
}
