package com.mygdx.game.client.model;

import com.badlogic.ashley.core.Entity;
import lombok.Data;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Level;

@Singleton
@Data
@Log
public class ActiveEntity {

  private Entity entity;

  @Inject
  public ActiveEntity() {
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
    log.log(Level.INFO, "Active Entity set");
  }

  public void clear() {
    this.entity = null;
  }
}
