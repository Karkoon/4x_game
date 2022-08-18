package com.mygdx.game.server.network;

import com.artemis.World;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import io.vertx.core.buffer.Buffer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Log
@Singleton
public class EndTurnService {

  private final GameRoom gameRoom;
  private String currentToken;

  private final Json json = new Json();

  @Inject
  EndTurnService(
      @NonNull GameRoom gameRoom
  ) {
    this.gameRoom = gameRoom;
  }

  public void init() {
    this.currentToken = gameRoom.getClient(0).getPlayerToken();
  }

  public void nextTurn(@NonNull Client client) {
    if (client.getPlayerToken().equals(currentToken)) {
      var clients = gameRoom.getClients().stream().toList();
      for (int i = 0; i < clients.size(); i++) {
        if (clients.get(i).getPlayerToken().equals(client.getPlayerToken())) {
          giveControlToPlayer(i + 1, clients);
        }
      }
    } else {
      log.info("Player " + client.getPlayerUsername() + " tried to end turn");
    }
  }

  private void giveControlToPlayer(int clientIndex, List<Client> clients) {
    var nextClient = getNextClient(clientIndex, clients);
    editPlayerToken(nextClient);
    log.info("Give control to player " + nextClient.getPlayerUsername());
    sendChangeTurnMessages();
  }

  private Client getNextClient(int clientIndex, List<Client> clients) {
    if (clients.size() == clientIndex) {
      return clients.get(0);
    } else {
      return clients.get(clientIndex);
    }
  }

  private void sendChangeTurnMessages() {
    var msg = new ChangeTurnMessage(this.currentToken);
    gameRoom.getClients().forEach(ws -> {
      var buffer = Buffer.buffer(json.toJson(msg, (Class<?>) null));
      ws.getSocket().write(buffer);
    });
  }

  private void editPlayerToken(Client nextClient) {
    this.currentToken = nextClient.getPlayerToken();
  }

}
