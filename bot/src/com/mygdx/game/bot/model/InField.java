package com.mygdx.game.bot.model;

import lombok.Data;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Data
@Log
@Singleton
public class InField {

  @Inject
  public InField() {
    super();
  }

  private boolean inField = false;
  private int field = -1;
}
