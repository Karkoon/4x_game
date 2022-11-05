package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoomManager;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectHandler {

  private final MessageSender sender;
  private final GameRoomManager rooms;

  @Inject
  public ConnectHandler(
      MessageSender sender,
      GameRoomManager rooms
  ) {
    this.sender = sender;
    this.rooms = rooms;
  }

  public void handle(String[] commands, Client client) {
    var userName = commands[1];
    var userToken = commands[2];
    var roomId = commands[3];
    long civId = Long.parseLong(commands[4]);
    client.setPlayerUsername(userName);
    client.setPlayerToken(userToken);
    client.setCivId(civId);
    var room = rooms.getRoom(roomId);
    room.addClient(client);
    client.setGameRoom(room);
    List<String> userNames = room.getClients()
      .stream()
      .map(Client::getPlayerUsername).collect(Collectors.toList());
    var msg = new PlayerJoinedRoomMessage(userNames);
    sender.sendToAll(msg, room.getClients());
  }
}
