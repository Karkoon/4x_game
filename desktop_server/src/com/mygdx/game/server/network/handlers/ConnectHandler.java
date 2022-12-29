package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.model.BotType;
import com.mygdx.game.core.model.PlayerLobby;
import com.mygdx.game.core.network.messages.PlayerAlreadyInTheRoomMessage;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import com.mygdx.game.core.network.messages.RoomConfigMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoomManager;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectHandler {

  private final MessageSender messageSender;
  private final GameRoomManager gameRoomManager;

  @Inject
  public ConnectHandler(
      GameRoomManager gameRoomManager,
      MessageSender messageSender
  ) {
    this.gameRoomManager = gameRoomManager;
    this.messageSender = messageSender;
  }

  public void handle(String[] commands, Client client) {
    var userName = commands[1];
    var userToken = commands[2];
    var roomId = commands[3];
    if (gameRoomManager.getRoom(roomId).getClients().stream().map(Client::getPlayerUsername).anyMatch(name -> name.equals(userName))) {
      var msg = new PlayerAlreadyInTheRoomMessage();
      messageSender.send(msg, client);
    } else {
      long civId = Long.parseLong(commands[4]);
      var playerType = commands[5];
      client.setPlayerUsername(userName);
      client.setPlayerToken(userToken);
      client.setCivId(civId);
      client.setBotType(BotType.valueOf(playerType));
      var room = gameRoomManager.getRoom(roomId);
      room.addClient(client);
      client.setGameRoom(room);
      List<PlayerLobby> users = room.getClients()
              .stream()
              .map(Client::mapToPlayerLobby).collect(Collectors.toList());
      var msg = new PlayerJoinedRoomMessage(users);
      messageSender.sendToAll(msg, room.getClients());
      var msg2 = new RoomConfigMessage(room.getMapSize(), room.getMapType());
      messageSender.send(msg2, client);
    }
  }
}
