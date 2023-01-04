package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.model.MapSize;
import com.mygdx.game.core.network.messages.RoomConfigMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoomManager;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;

public class ChangeLobbyHandler {

  private final MessageSender messageSender;
  private final GameRoomManager gameRoomManager;

  @Inject
  public ChangeLobbyHandler(
      GameRoomManager gameRoomManager,
      MessageSender messageSender
  ) {
    this.gameRoomManager = gameRoomManager;
    this.messageSender = messageSender;
  }

  public void handle(String[] commands, Client client) {
    MapSize mapSize = MapSize.valueOf(commands[1]);
    int mapType = Integer.parseInt(commands[2]);
    var room = gameRoomManager.getRoom(client.getGameRoom().getRoomId());
    room.setMapSize(mapSize);
    room.setMapType(mapType);
    var msg = new RoomConfigMessage(room.getMapSize(), room.getMapType());
    messageSender.sendToAll(msg, room.getClients());
  }
}
