package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.network.messages.GameInterruptedMessage;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;

public class InterruptService {

  private final GameRoom gameRoom;
  private final MessageSender messageSender;

  @Inject
  InterruptService(
      GameRoom gameRoom,
      MessageSender messageSender
  ) {
    super();
    this.gameRoom = gameRoom;
    this.messageSender = messageSender;
  }

  public void notifyOfInterrupt(String exceptPlayerToken) {
    var msg = new GameInterruptedMessage();
    if (gameRoom.getGameInstance() != null) {
      gameRoom.tearDownGameInstance();
      messageSender.sendToAll(msg, gameRoom.getClients().stream().filter(c -> !c.getPlayerToken().equals(exceptPlayerToken)).toList());
    }
  }

}
