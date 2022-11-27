package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.model.MapSize;
import com.mygdx.game.core.network.messages.RoomConfigMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoomManager;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;

public class ChangeLobbyHandler {

  private final MessageSender sender;
  private final GameRoomManager rooms;

  @Inject
  public ChangeLobbyHandler(
      MessageSender sender,
      GameRoomManager rooms
  ) {
    this.sender = sender;
    this.rooms = rooms;
  }

  public void handle(String[] commands, Client client) {
    var mapSize = MapSize.valueOf(commands[1]);
    int mapType = Integer.parseInt(commands[2]);
    var room = rooms.getRoom(client.getGameRoom().getRoomId());
    room.setMapSize(mapSize);
    room.setMapType(mapType);
    var msg = new RoomConfigMessage(room.getMapSize(), room.getMapType());
    sender.sendToAll(msg, room.getClients());
  }
}
