package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.GameRoomSyncer;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;

public class StartHandler {

  private final GameRoomSyncer syncer;
  private final MessageSender sender;

  @Inject
  public StartHandler(
      GameRoomSyncer syncer,
      MessageSender sender
  ) {
    this.syncer = syncer;
    this.sender = sender;
  }

  public void handle(String[] commands, Client client) {
    var width = Integer.parseInt(commands[1]);
    var height = Integer.parseInt(commands[2]);
    var mapType = Long.parseLong(commands[3]);
    var room = client.getGameRoom();
    room.setupGameInstance();
    syncer.beginTransaction(room);
    room.getGameInstance().startGame(width, height, mapType);
    syncer.endTransaction(room);
    var msg = new GameStartedMessage(room.getClients().get(0).getPlayerToken());
    sender.sendToAll(msg, room.getClients());
  }
}
