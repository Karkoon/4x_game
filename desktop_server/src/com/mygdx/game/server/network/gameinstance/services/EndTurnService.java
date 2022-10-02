package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameInstance;
import com.mygdx.game.server.network.MessageSender;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class EndTurnService {

  private final MessageSender sender;
  private final EndTurnUtils endTurnUtils;

  @Inject
  EndTurnService(
      MessageSender sender,
      EndTurnUtils endTurnUtils
  ) {
    super();
    this.sender = sender;
    this.endTurnUtils = endTurnUtils;
  }

  public void nextTurn(@NonNull Client client) {
    var gameInstance = client.getGameRoom().getGameInstance();
    var currentPlayer = gameInstance.getActivePlayer();
    if(clientHasToken(client, currentPlayer)) {
      endTurnOfAllPlayersIfLastPlayer(currentPlayer, gameInstance);
      giveControlToNextPlayer(client, gameInstance);
    }
  }

  private boolean clientHasToken(Client client, Client currentPlayer) {
    if (!client.getPlayerToken().equals(currentPlayer.getPlayerToken())) {
      log.info("Player " + client.getPlayerUsername() + " tried to end turn");
      return false;
    }
    return true;
  }

  private void endTurnOfAllPlayersIfLastPlayer(Client client, GameInstance gameInstance) {
    if (gameInstance.isLastPlayer()) {
      log.info("End turn, edit components");
      endTurnUtils.makeEndTurnSteps();
    }
  }

  private void giveControlToNextPlayer(Client client, GameInstance gameInstance) {
    var nextClient = gameInstance.changeToNextPlayer();
    log.info("Give control to player " + nextClient.getPlayerUsername());
    var msg = new ChangeTurnMessage(nextClient.getPlayerToken());
    sender.sendToAll(msg, client.getGameRoom().getClients());
  }


}
