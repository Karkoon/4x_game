package com.mygdx.game.server.network;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.core.network.messages.ComponentMessage;

public class Transaction {
  private final Array<ComponentMessage<?>> messageBuffer = new Array<>(false, 16);

  public void addToBuffer(ComponentMessage<?> componentMessage) {
    messageBuffer.add(componentMessage);
  }

  public boolean isPending() {
    return !messageBuffer.isEmpty();
  }

  public Array<ComponentMessage<?>> getMessageBuffer() {
    return new Array<>(messageBuffer);
  }

  public void clear() {
    messageBuffer.clear();
  }
}
