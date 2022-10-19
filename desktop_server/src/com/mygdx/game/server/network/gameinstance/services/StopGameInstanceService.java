package com.mygdx.game.server.network.gameinstance.services;

import com.google.inject.Inject;
import com.mygdx.game.core.network.messages.StopGameInstanceMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;

@GameInstanceScope
public class StopGameInstanceService {

  private final GameRoom gameRoom;
  private final MessageSender sender;

  @Inject
  public StopGameInstanceService(
      GameRoom gameRoom,
      MessageSender sender
  ) {
    this.gameRoom = gameRoom;
    this.sender = sender;
  }

  public void sendStopGameInstanceMessageToAll(Client originator) {
    gameRoom.getClients().stream().filter(client -> client != originator).forEach(this::sendMessage);
  }

  private void sendMessage(Client client) {
    sender.send(new StopGameInstanceMessage(), client);
  }
}
