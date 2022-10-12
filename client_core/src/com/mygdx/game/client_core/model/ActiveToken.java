package com.mygdx.game.client_core.model;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActiveToken {
  private String token = "";

  @Inject
  public ActiveToken() {
    super();
  }


  public void setActiveToken(String token) {
    this.token = token;
  }

  public boolean isActiveToken(String token) {
    return token.equals(this.token);
  }

}
