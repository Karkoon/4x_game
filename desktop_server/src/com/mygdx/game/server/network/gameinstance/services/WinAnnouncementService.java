package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.network.messages.WinAnnouncementMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;
import com.mygdx.game.server.network.gameinstance.StateSyncer;

import javax.inject.Inject;

@GameInstanceScope
public class WinAnnouncementService {
  private final StateSyncer sender;
  private final GameRoom room;

  @Inject
  WinAnnouncementService(
      StateSyncer sender,
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
    for (int i = 0; i < room.getClients().size(); i++) {
      sender.sendObjectTo(msg, room.getClients().get(i));
    }
  }


}
