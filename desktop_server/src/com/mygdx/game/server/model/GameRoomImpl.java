package com.mygdx.game.server.model;

import com.mygdx.game.server.di.GameInstanceSubcomponent;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
public class GameRoomImpl implements GameRoom {

  private final Map<String, Client> clients = new HashMap<>();
  private final String roomId;
  private final GameInstanceSubcomponent.Builder gameInstanceSubcomponentBuilder;
  private GameInstance activeGameInstance;

  @AssistedInject
  public GameRoomImpl(
      GameInstanceSubcomponent.Builder gameInstanceSubcomponentBuilder,
      @Assisted String roomId
  ) {
    this.roomId = roomId;
    this.gameInstanceSubcomponentBuilder = gameInstanceSubcomponentBuilder;
  }

  public List<Client> getClients() { // przesortować jakoś pewnie, ustalić kolejność
    return clients.values().stream().sorted(Comparator.comparing(Client::getPlayerUsername)).toList();
  }

  public void addClient(@NonNull Client client) {
    log.info("added client " + client.getPlayerUsername());
    clients.put(client.getPlayerToken(), client);
  }

  public void removeClient(@NonNull Client client) {
    log.info("removed client " + client.getPlayerUsername());
    clients.remove(client.getPlayerToken());
  }

  public int getNumberOfClients() {
    return clients.size();
  }

  public String getRoomId() {
    return roomId;
  }

  public void setupGameInstance() {
    log.info("started game");
    activeGameInstance = gameInstanceSubcomponentBuilder.gameRoom(this).build().get();
  }

  public void tearDownGameInstance() {
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