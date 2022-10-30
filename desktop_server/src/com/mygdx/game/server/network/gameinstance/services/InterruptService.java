package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.network.messages.GameInterruptedMessage;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;

public class InterruptService {
  private final MessageSender sender;
  private final GameRoom room;

  @Inject
  InterruptService(
      MessageSender sender,
      GameRoom room
  ) {
    super();
    this.sender = sender;
    this.room = room;
  }

  public void notifyOfInterrupt(String exceptPlayerToken) {
    var msg = new GameInterruptedMessage();
    if (room.getGameInstance() != null) {
      room.tearDownGameInstance();
      sender.sendToAll(msg, room.getClients().stream().filter(c -> !c.getPlayerToken().equals(exceptPlayerToken)).toList());
    }
  }

}
