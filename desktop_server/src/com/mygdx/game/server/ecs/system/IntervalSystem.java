package com.mygdx.game.server.ecs.system;

import com.artemis.BaseSystem;

public abstract class IntervalSystem extends BaseSystem {

  private final float interval;
  private float acc;
  private float intervalDelta;

  IntervalSystem(float interval) {
    this.interval = interval;
  }

  @Override
  protected boolean checkProcessing() {
    if (intervalDelta > 0 && acc == 0) {
      intervalDelta = 0;
    }

    acc += world.getDelta();
    if (acc >= interval) {
      intervalDelta = acc;
      acc = 0;

      return true;
    }
    return false;
  }
}
