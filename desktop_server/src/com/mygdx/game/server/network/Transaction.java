package com.mygdx.game.server.network;

import com.badlogic.gdx.utils.Array;

public class Transaction {

  private final Array<Object> messageBuffer = new Array<>(false, 16);

  public void addToBuffer(Object componentMessage) {
    messageBuffer.add(componentMessage);
  }

  public boolean isPending() {
    return !messageBuffer.isEmpty();
  }

  public Array<Object> getMessageBuffer() {
    return new Array<>(messageBuffer);
  }

  public void clear() {
    messageBuffer.clear();
  }
}
