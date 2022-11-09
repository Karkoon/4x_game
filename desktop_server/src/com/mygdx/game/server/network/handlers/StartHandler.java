package com.mygdx.game.server.network.handlers;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.MessageSender;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class StartHandler {

  private final MessageSender sender;

  @Inject
  public StartHandler(
      MessageSender sender
  ) {
    this.sender = sender;
  }

  public void handle(String[] commands, Client client) {
    Gdx.app.postRunnable(() -> {
      var mapType = Long.parseLong(commands[1]);
      var room = client.getGameRoom();
      var msg = new GameStartedMessage(room.getClients().get(0).getPlayerToken());
      log.info("GameStartedMessage: " + msg + " room " + room.getClients().toString());
      sender.sendToAll(msg, room.getClients());
      room.setupGameInstance();
      room.getGameInstance().startGame(mapType);
    });
  }
}
