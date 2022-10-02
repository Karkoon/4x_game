package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.GameInstance;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class EndTurnService {

  private final MessageSender sender;
  private final GameRoom room;
  private final GameInstance gameInstance;

  @Inject
  EndTurnService(
      MessageSender sender,
      GameRoom room,
      GameInstance gameInstance
  ) {
    super();
    this.sender = sender;
    this.room = room;
    this.gameInstance = gameInstance;
  }

  public void nextTurn() {
    var nextClient = gameInstance.changeToNextPlayer();
    log.info("Give control to player " + nextClient.getPlayerUsername());
    var msg = new ChangeTurnMessage(nextClient.getPlayerToken());
    sender.sendToAll(msg, room.getClients());
  }

}
