package com.mygdx.game.server.network;

import io.vertx.core.http.ServerWebSocket;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ClientManager {
  private final Map<Integer, ServerWebSocket> clients = new HashMap<>();

  private int nextId = 0;

  @Inject
  public ClientManager() {

  }

  public @NonNull ServerWebSocket getClient(int clientId) {
    return clients.get(clientId);
  }

  public Collection<ServerWebSocket> getClients() {
    return clients.values();
  }

  public int addClient(@NonNull ServerWebSocket websocket) {
    clients.put(nextId, websocket);
    return nextId++;
  }

  public void removeClient(int id) {
    clients.remove(id);
  }
}
