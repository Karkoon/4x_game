package com.mygdx.game.client.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnitMovementComp implements Component, Pool.Poolable {

  private Entity fromEntity;
  private Entity toEntity;

  public void setFromAndTo(Entity from, Entity to) {
    this.fromEntity = from;
    this.toEntity = to;
  }

  @Override
  public void reset() {
    this.fromEntity = null;
    this.toEntity = null;
  }
}
