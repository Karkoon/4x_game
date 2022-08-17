package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.GameRoomSyncer;
import com.mygdx.game.server.network.MessageSender;

import javax.inject.Inject;

public class StartHandler {

  private final GameRoomSyncer syncer;
  private final MessageSender messageSender;

  @Inject
  StartHandler(
      GameRoomSyncer syncer,
      MessageSender messageSender
  ) {
    this.syncer = syncer;
    this.messageSender = messageSender;
  }

  public void handle(String[] commands, Client client) {
    var width = Integer.parseInt(commands[1]);
    var height = Integer.parseInt(commands[2]);
    var mapType = Integer.parseInt(commands[3]);
    var room = client.getGameRoom();
    room.startGame();
    var gameInstance = room.getGameInstance();
    syncer.beginTransaction(room);
    gameInstance.startGame(width, height, mapType);
    syncer.endTransaction(room);
    var msg = new GameStartedMessage();
    messageSender.sendToAll(msg, room.getClients());
  }
}
