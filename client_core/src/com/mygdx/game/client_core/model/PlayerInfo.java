package com.mygdx.game.client_core.model;

import com.mygdx.game.config.GameConfigs;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;
import java.util.UUID;

@Getter
@Singleton
public class PlayerInfo {

  private String userName;
  private final String token;
  private long civilization;

  @Inject
  public PlayerInfo() { // TODO: 10.10.2022 make username configurable
    token = UUID.randomUUID().toString();
    civilization = new Random().nextLong(GameConfigs.CIV_MAX+ 1 - GameConfigs.CIV_MIN) + GameConfigs.CIV_MIN;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setCivilization(long civilization) {
    this.civilization = civilization;
  }
}
