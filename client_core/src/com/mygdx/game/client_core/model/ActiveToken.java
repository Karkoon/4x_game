package com.mygdx.game.client_core.model;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;

import javax.inject.Inject;

@GameInstanceScope
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
