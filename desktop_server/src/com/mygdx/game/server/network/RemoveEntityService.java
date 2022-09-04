package com.mygdx.game.server.network;

import com.mygdx.game.core.network.messages.RemoveEntityMessage;
import com.mygdx.game.server.model.Client;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class RemoveEntityService {

  private final MessageSender sender;

  @Inject
  RemoveEntityService(
      MessageSender sender
  ) {
    super();
    this.sender = sender;
  }

  public void removeEntity(int entityId, @NonNull Client client) {
    var msg = new RemoveEntityMessage(entityId);
    sender.send(msg, client);
  }
}
