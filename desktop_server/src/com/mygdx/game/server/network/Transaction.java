package com.mygdx.game.server.network;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.core.network.messages.ComponentMessage;
import lombok.Data;

@Data
public class Transaction {
  private final Array<ComponentMessage<?>> messageBuffer = new Array<>(false, 16);

  public void addToBuffer(ComponentMessage<?> componentMessage) {
    messageBuffer.add(componentMessage);
  }

  public boolean isPending() {
    return !messageBuffer.isEmpty();
  }
}
