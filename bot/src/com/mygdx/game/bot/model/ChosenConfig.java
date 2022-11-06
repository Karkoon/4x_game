package com.mygdx.game.bot.model;

import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class ChosenConfig {

  private static final int NO_ENTITY = -0xC0FFE;
  private static final Class<?> NO_ENTITY_CLASS = null;

  private long entityConfigId = NO_ENTITY;
  private Class<?> entityConfigClass = NO_ENTITY_CLASS;

  @Inject
  public ChosenConfig() {
    super();
  }

  public void addChosen(long entityConfigId, Class<?> entityConfigClass) {
    log.info("chosen config");
    this.entityConfigId = entityConfigId;
    this.entityConfigClass = entityConfigClass;
  }

  public boolean isAnyChosen() {
    return entityConfigId != NO_ENTITY;
  }

  public void clear() {
    entityConfigId = NO_ENTITY;
    entityConfigClass = NO_ENTITY_CLASS;
  }

  public long pop() {
    var value = entityConfigId;
    clear();
    return value;
  }

  public long peek() {
    return entityConfigId;
  }

  public Class<?> peekClass() {
    return entityConfigClass;
  }
}
