package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.model.PlayerLobby;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoomManager;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveHandler {

  private final MessageSender sender;
  private final GameRoomManager rooms;

  @Inject
  public RemoveHandler(
      MessageSender sender,
      GameRoomManager rooms
  ) {
    this.sender = sender;
    this.rooms = rooms;
  }

  public void handle(String[] commands, Client client) {
    var userName = commands[1];
    var gameRoom = client.getGameRoom();
    for (Client client1 : gameRoom.getClients()) {
      if (client1.getPlayerUsername().equals(userName)) {
        gameRoom.removeClient(client1);
        break;
      }
    }
    List<PlayerLobby> users = gameRoom.getClients()
            .stream()
            .map(Client::mapToPlayerLobby).collect(Collectors.toList());
    var msg = new PlayerJoinedRoomMessage(users);
    sender.sendToAll(msg, gameRoom.getClients());
  }
}
