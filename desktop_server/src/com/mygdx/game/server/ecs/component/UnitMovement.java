package com.mygdx.game.server.ecs.component;

import com.artemis.PooledComponent;
import com.mygdx.game.core.ecs.component.Slot;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UnitMovement extends PooledComponent {

  private @NonNull Slot fromSlot;
  private @NonNull Slot toSlot;

  @Override
  public void reset() {
    this.fromSlot = null;
    this.toSlot = null;
  }
}
