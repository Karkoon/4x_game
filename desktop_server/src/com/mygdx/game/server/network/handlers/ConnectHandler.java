package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoomManager;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;

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
    client.setPlayerUsername(userName);
    client.setPlayerToken(userToken);
    var room = rooms.getRoom(roomId);
    room.addClient(client);
    client.setGameRoom(room);
    var msg = new PlayerJoinedRoomMessage(room.getNumberOfClients());
    sender.sendToAll(msg, room.getClients());
  }
}
