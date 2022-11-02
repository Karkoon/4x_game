package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.network.messages.WinAnnouncementMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;

@GameInstanceScope
public class WinAnnouncementService {
  private final MessageSender sender;
  private final GameRoom room;

  @Inject
  WinAnnouncementService(
      MessageSender sender,
      GameRoom room
  ) {
    super();
    this.sender = sender;
    this.room = room;
  }

  public void notifyOfWinner(String winnerToken) {
    var winnerName = room.getClientByToken(winnerToken).getPlayerUsername();
    var msg = new WinAnnouncementMessage(winnerToken, winnerName);
    room.tearDownGameInstance();
    sender.sendToAll(msg, room.getClients());
  }


}
