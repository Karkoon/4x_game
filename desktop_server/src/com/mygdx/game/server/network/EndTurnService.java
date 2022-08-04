package com.mygdx.game.server.network;

import com.artemis.World;
import com.mygdx.game.core.ecs.component.PlayerToken;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.List;

@Log
public class EndTurnService {

  private final int playerTokenComponentId;
  private final PlayerToken playerToken;
  private final GameRoom gameRoom;
  private final GameRoomSyncer gameRoomSyncer;

  @Inject
  EndTurnService(
      @NonNull World world,
      @NonNull ComponentFactory componentFactory,
      @NonNull GameRoom gameRoom,
      @NonNull GameRoomSyncer gameRoomSyncer
  ) {
    this.playerTokenComponentId = componentFactory.createEntityId();
    this.playerToken = world.getMapper(PlayerToken.class).create(this.playerTokenComponentId);
    this.gameRoom = gameRoom;
    this.gameRoomSyncer = gameRoomSyncer;
  }

  public void giveTurnToFirstPlayer() {
    var clients = gameRoom.getClients().stream().toList();
    log.info("First turn, start player " + clients.get(0).getPlayerUsername());
    giveControlToPlayer(0, clients);
  }

  public void nextTurn(String playerNickname) {
    log.info("Give control to player " + playerNickname);
    var clients = gameRoom.getClients().stream().toList();
    for (int i = 0; i < clients.size(); i++) {
      if (clients.get(i).getPlayerUsername().equals(playerNickname)) {
        giveControlToPlayer(i + 1, clients);
      }
    }
  }

  private void giveControlToPlayer(int clientIndex, List<Client> clients) {
     var nextClient = getNextClient(clientIndex, clients);
     editPlayerTokenComponent(nextClient);
     gameRoomSyncer.sendComponent(playerToken, playerTokenComponentId);
  }

  private Client getNextClient(int clientIndex, List<Client> clients) {
    if (clients.size() == clientIndex) {
      return clients.get(0);
    } else {
      return clients.get(clientIndex);
    }
  }

  private void editPlayerTokenComponent(Client nextClient) {
    playerToken.setToken(nextClient.getPlayerToken());
  }
}
