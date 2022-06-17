package com.mygdx.game.core.network;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.core.service.SyncListener;
import lombok.NonNull;

// TODO: 04.06.2022 use it for networking?
public abstract class Syncer {
  private final Array<SyncListener<?>> listeners = new Array<>();

  public void addSyncListener(@NonNull SyncListener<?> listener) {
    listeners.add(listener);
  }
}
