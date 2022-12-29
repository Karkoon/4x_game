package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.GameInstance;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class EndTurnService {

  private final MessageSender messageSender;
  private final GameRoom gameRoom;
  private final GameInstance gameInstance;

  @Inject
  EndTurnService(
      GameRoom gameRoom,
      GameInstance gameInstance,
      MessageSender messageSender
      ) {
    super();
    this.gameRoom = gameRoom;
    this.gameInstance = gameInstance;
    this.messageSender = messageSender;
  }

  public void nextTurn() {
    var nextClient = gameInstance.changeToNextPlayer();
    log.info("Give control to player " + nextClient.getPlayerUsername());
    var msg = new ChangeTurnMessage(nextClient.getPlayerToken());
    messageSender.sendToAll(msg, gameRoom.getClients());
  }

}
