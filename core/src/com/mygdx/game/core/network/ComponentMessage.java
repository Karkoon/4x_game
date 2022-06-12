package com.mygdx.game.core.network;

import com.artemis.Component;

public class ComponentMessage<T extends Component> {
  private final T component;
  private final int entityId;

  public ComponentMessage(T component, int entityId) {
    this.component = component;
    this.entityId = entityId;
  }

  public T getComponent() {
    return component;
  }

  public int getEntityId() {
    return entityId;
  }

  @Override
  public String toString() {
    return "ComponentMessage{" +
        "component=" + component +
        ", entityId=" + entityId +
        '}';
  }
}
