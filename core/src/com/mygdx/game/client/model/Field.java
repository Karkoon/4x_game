package com.mygdx.game.client.model;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.config.FieldConfig;
import lombok.NonNull;

public class Field extends GameEntity<FieldConfig> {

  public Field(int xPosition, int yPosition, @NonNull FieldConfig fieldConfig, @NonNull Entity entity) {
    super(xPosition, yPosition, fieldConfig, entity);
  }

}
