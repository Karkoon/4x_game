package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.network.messages.WinAnnouncementMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;

@GameInstanceScope
public class WinAnnouncementService {

  private final GameRoom gameRoom;
  private final MessageSender messageSender;

  @Inject
  WinAnnouncementService(
      GameRoom gameRoom,
      MessageSender messageSender
      ) {
    super();
    this.gameRoom = gameRoom;
    this.messageSender = messageSender;
  }

  public void notifyOfWinner(String winnerToken) {
    var winnerName = gameRoom.getClientByToken(winnerToken).getPlayerUsername();
    var msg = new WinAnnouncementMessage(winnerToken, winnerName);
    gameRoom.tearDownGameInstance();
    messageSender.sendToAll(msg, gameRoom.getClients());
  }


}
