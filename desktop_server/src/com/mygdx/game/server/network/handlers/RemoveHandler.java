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

  private final GameRoomManager gameRoomManager;
  private final MessageSender messageSender;

  @Inject
  public RemoveHandler(
      GameRoomManager gameRoomManager,
      MessageSender messageSender
      ) {
    this.gameRoomManager = gameRoomManager;
    this.messageSender = messageSender;
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
    messageSender.sendToAll(msg, gameRoom.getClients());
  }
}
