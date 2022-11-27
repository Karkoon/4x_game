package com.mygdx.game.server.network.handlers;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.core.model.PlayerLobby;
import com.mygdx.game.core.network.messages.PlayerLobbyChangedMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;

public class RemoveHandler {

  private final MessageSender sender;

  @Inject
  public RemoveHandler(
      MessageSender sender
  ) {
    this.sender = sender;
  }

  public void handle(String[] commands, Client client) {
    var userName = commands[1];
    var room = client.getGameRoom();
    for (Client client1 : room.getClients()) {
      if (client1.getPlayerUsername().equals(userName)) {
        room.removeClient(client1);
        break;
      }
    }
    var players = new PlayerLobby[room.getClients().size()];
    for (int i = 0; i < room.getClients().size(); i++) {
      players[i] = room.getClients().get(i).mapToPlayerLobby();
    }
    var msg = new PlayerLobbyChangedMessage(new Array<>(players));
    sender.sendToAll(msg, room.getClients());
  }
}
