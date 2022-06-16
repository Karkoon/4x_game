package com.mygdx.game.server.model;

import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class GameRoom {

  private final Map<Integer, Client> clients = new HashMap<>();
  private int nextId = 0;

  @Inject
  public GameRoom() {

  }

  public @NonNull Client getClient(int clientId) {
    return clients.get(clientId);
  }

  public Collection<Client> getClients() {
    return clients.values();
  }

  public void addClient(@NonNull Client client) {
    client.setId(nextId);
    clients.put(nextId++, client);
  }

  public void removeClient(int id) {
    clients.remove(id);
  }

  public int getNumberOfClients() {
    return clients.size();
  }

}
