package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.model.PlayerLobby;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoomManager;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ReconnectHandler {

  private final MessageSender sender;
  private final GameRoomManager rooms;

  @Inject
  public ReconnectHandler(
      MessageSender sender,
      GameRoomManager rooms
  ) {
    this.sender = sender;
    this.rooms = rooms;
  }

  public void handle(String[] commands, Client client) {
    long civId = Long.parseLong(commands[1]);
    client.setCivId(civId);
    var room = rooms.getRoom(client.getGameRoom().getRoomId());
    List<PlayerLobby> users = room.getClients()
            .stream()
            .map(Client::mapToPlayerLobby).collect(Collectors.toList());
    var msg = new PlayerJoinedRoomMessage(users);
    sender.sendToAll(msg, room.getClients());
  }
}
