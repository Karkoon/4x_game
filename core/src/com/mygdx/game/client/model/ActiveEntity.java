package com.mygdx.game.client.model;

import com.badlogic.ashley.core.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Level;

@Singleton
@Data
@Log
public class ActiveEntity {

  private ActiveEntityType entityType;
  private Entity entity;

  @Inject
  public ActiveEntity() {
  }

  public void setEntity(Entity entity, @NonNull ActiveEntityType entityType) {
    this.entity = entity;
    this.entityType = entityType;
    log.log(Level.INFO, "Active Entity: " + entityType);
  }

  public void clear() {
    this.entity = null;
    this.entityType = ActiveEntityType.EMPTY;
  }


}
