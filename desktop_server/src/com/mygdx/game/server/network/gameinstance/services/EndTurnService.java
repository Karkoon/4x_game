package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.MessageSender;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class EndTurnService {

  private final MessageSender sender;

  @Inject
  EndTurnService(
      MessageSender sender
  ) {
    super();
    this.sender = sender;
  }

  public void nextTurn(@NonNull Client client) {
    var gameInstance = client.getGameRoom().getGameInstance();
    var currentPlayer = gameInstance.getActivePlayer();
    if (!client.getPlayerToken().equals(currentPlayer.getPlayerToken())) {
      log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "Player " + client.getPlayerUsername() + " tried to end turn");
    }
    var nextClient = gameInstance.changeToNextPlayer();
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "Give control to player " + nextClient.getPlayerUsername());
    var msg = new ChangeTurnMessage(nextClient.getPlayerToken());
    sender.sendToAll(msg, client.getGameRoom().getClients());
  }
}
