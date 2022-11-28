package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.GameInstance;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;
import com.mygdx.game.server.network.gameinstance.StateSyncer;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class EndTurnService {

  private final StateSyncer sender;
  private final GameRoom room;
  private final GameInstance gameInstance;

  @Inject
  EndTurnService(
      StateSyncer sender,
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
    for (int i = 0; i < room.getClients().size(); i++) {
      var client = room.getClients().get(i);
      sender.sendObjectTo(msg, client);
    }
  }
}
