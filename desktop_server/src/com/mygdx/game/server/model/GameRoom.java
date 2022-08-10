package com.mygdx.game.server.model;

import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

@Singleton
public class GameRoom {

  private final Queue<Client> clients = new ArrayDeque<>();
  private Client activePlayer;

  @Inject
  public GameRoom() {
    super();
  }

  public void setNextActivePlayer() {
    activePlayer = clients.remove();
    clients.add(activePlayer);
  }

  public Collection<Client> getClients() {
    return clients;
  }

  public void addClient(@NonNull Client client) {
    clients.add(client);
  }

  public int getNumberOfClients() {
    return clients.size();
  }

  public Client getActivePlayer() {
    return activePlayer;
  }
}
