package com.mygdx.game.server.network;

import com.badlogic.gdx.utils.Json;
import com.mygdx.game.core.network.messages.ActivePlayerMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import io.vertx.core.buffer.Buffer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class EndTurnService {

  private final GameRoom gameRoom;
  private final Json json = new Json();

  @Inject
  EndTurnService(
      @NonNull GameRoom gameRoom
  ) {
    this.gameRoom = gameRoom;
  }

  public void nextTurn(@NonNull Client nextTurnRequester) {
    var gameInProgress = gameRoom.getActivePlayer() != null;
    var requesterIsNotValid = !nextTurnRequester.equals(gameRoom.getActivePlayer());
    if (gameInProgress && requesterIsNotValid) {
      log.info("Player " + nextTurnRequester.getPlayerUsername() + " tried to end turn");
      return;
    }
    gameRoom.setNextActivePlayer();
    gameRoom.getClients().forEach(this::sendActivePlayerMessage);
  }

  private void sendActivePlayerMessage(Client client) {
    var activePlayer = gameRoom.getActivePlayer();
    var activeTurnMessage = new ActivePlayerMessage(activePlayer.getPlayerUsername());
    sendSavingClassInJson(activeTurnMessage, client);
  }

  private void sendSavingClassInJson(Object message, Client client) {
    var jsonString = json.toJson(message, (Class<?>) null);
    client.getSocket().write(Buffer.buffer(jsonString));
  }
}
