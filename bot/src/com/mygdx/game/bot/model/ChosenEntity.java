package com.mygdx.game.bot.model;

import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class ChosenEntity {

  private static final int NO_ENTITY = -0xC0FFE;

  private int entity = NO_ENTITY;

  @Inject
  public ChosenEntity() {
    super();
  }

  public void addChosen(int entity) {
    log.info("chosen entity");
    this.entity = entity;
  }

  public boolean isAnyChosen() {
    return entity != NO_ENTITY;
  }

  public void clear() {
    entity = NO_ENTITY;
  }

  public int pop() {
    var value = entity;
    clear();
    return value;
  }

  public int peek() {
    return entity;
  }

}
