package com.mygdx.game.client.model;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;

public class Unit extends GameEntity<UnitConfig> {

  public Unit(int xPosition, int yPosition, @NonNull UnitConfig unitConfig, @NonNull Entity entity) {
    super(xPosition, yPosition, unitConfig, entity);
  }

}


