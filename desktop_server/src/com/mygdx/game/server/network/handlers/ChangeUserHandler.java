package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.model.BotType;
import com.mygdx.game.core.model.PlayerLobby;
import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoomManager;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeUserHandler {

  private final MessageSender sender;
  private final GameRoomManager rooms;

  @Inject
  public ChangeUserHandler(
      MessageSender sender,
      GameRoomManager rooms
  ) {
    this.sender = sender;
    this.rooms = rooms;
  }

  public void handle(String[] commands, Client client) {
    String userName = commands[1];
    long civId = Long.parseLong(commands[2]);
    BotType botType = BotType.valueOf(commands[3]);
    List<Client> clients = client.getGameRoom().getClients();
    for (Client oneClient : clients) {
      if (oneClient.getPlayerUsername().equals(userName)) {
        oneClient.setCivId(civId);
        if (oneClient.getBotType() != BotType.NOT_BOT) {
          oneClient.setBotType(botType);
        }
        break;
      }
    }
    var room = rooms.getRoom(client.getGameRoom().getRoomId());
    List<PlayerLobby> users = room.getClients()
            .stream()
            .map(Client::mapToPlayerLobby).collect(Collectors.toList());
    var msg = new PlayerJoinedRoomMessage(users);
    sender.sendToAll(msg, room.getClients());
  }
}
