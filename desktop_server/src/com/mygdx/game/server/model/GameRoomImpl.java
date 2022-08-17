package com.mygdx.game.server.model;

import com.mygdx.game.server.di.GameInstanceSubcomponent;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Log
public class GameRoomImpl implements GameRoom {

  private final Map<Integer, Client> clients = new HashMap<>();
  private final String roomId;
  private final GameInstanceSubcomponent gameInstanceSubcomponent;
  private GameInstance activeGameInstance;

  private int nextId = 0;

  @AssistedInject
  public GameRoomImpl(
      GameInstanceSubcomponent.Builder gameInstanceSubcomponentBuilder,
      @Assisted String roomId
  ) {
    this.roomId = roomId;
    this.gameInstanceSubcomponent = gameInstanceSubcomponentBuilder.gameRoom(this).build();
  }

  public Collection<Client> getClients() {
    return clients.values();
  }

  public void addClient(@NonNull Client client) {
    client.setId(nextId);
    log.info("added client " + client.getId());
    clients.put(nextId++, client);
  }

  public void removeClient(@NonNull Client client) {
    log.info("removed client " + client.getId());
    clients.remove(client.getId());
    if (clients.size() == 0) {
      stopGame();
    }
  }

  public int getNumberOfClients() {
    return clients.size();
  }

  public String getRoomId() {
    return roomId;
  }

  public void startGame() {
    log.info("started game");
    activeGameInstance = gameInstanceSubcomponent.get();
  }

  public void stopGame() {
    log.info("stopped game");
    activeGameInstance = null;
  }

  @NonNull
  public GameInstance getGameInstance() {
    return activeGameInstance;
  }

  @AssistedFactory
  public interface Factory {
    GameRoomImpl get(
        String roomId
    );
  }
}
