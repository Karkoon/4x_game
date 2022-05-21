package com.mygdx.game.client.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UnitMovement implements Component, Pool.Poolable {

  private @NonNull Entity fromEntity;
  private @NonNull Entity toEntity;

  @Override
  public void reset() {
    this.fromEntity = null;
    this.toEntity = null;
  }
}
