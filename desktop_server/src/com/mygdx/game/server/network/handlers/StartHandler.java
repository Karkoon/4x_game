package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.GameRoomSyncer;
import com.mygdx.game.server.network.MessageSender;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
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
    var msg = new GameStartedMessage(room.getClients().get(0).getPlayerToken());
    log.info("GameStartedMessage: " + msg + " room " + room.getClients().toString());
    sender.sendToAll(msg, room.getClients());
    room.setupGameInstance();
    syncer.beginTransaction(room);
    room.getGameInstance().startGame(width, height, mapType);
    syncer.endTransaction(room);
  }
}
