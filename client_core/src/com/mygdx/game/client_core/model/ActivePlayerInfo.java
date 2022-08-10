package com.mygdx.game.client_core.model;

import lombok.Data;

import javax.inject.Inject;
import javax.inject.Singleton;

@Data
@Singleton
public class ActivePlayerInfo {

  @Inject
  public ActivePlayerInfo() {
    super();
  }

  private String username;
}
