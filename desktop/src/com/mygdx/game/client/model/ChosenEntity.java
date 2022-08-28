package com.mygdx.game.client.model;


import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@GameInstanceScope
public class ChosenEntity {
  private static final int NO_ENTITY = -0xC0FFE;

  private int entity = NO_ENTITY;

  @Inject
  public ChosenEntity() {
    super();
  }

  public void addChosen(int entity) {
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "chosen entity");
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
