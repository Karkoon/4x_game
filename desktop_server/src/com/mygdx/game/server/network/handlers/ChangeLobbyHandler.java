package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.model.MapSize;
import com.mygdx.game.core.model.PlayerLobby;
import com.mygdx.game.core.network.messages.RoomConfigMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoomManager;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

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
    MapSize mapSize = MapSize.valueOf(commands[1]);
    var room = rooms.getRoom(client.getGameRoom().getRoomId());
    room.setMapSize(mapSize);
    List<PlayerLobby> users = room.getClients()
            .stream()
            .map(Client::mapToPlayerLobby).collect(Collectors.toList());
    var msg = new RoomConfigMessage(room.getMapSize());
    sender.sendToAll(msg, room.getClients());
  }
}
