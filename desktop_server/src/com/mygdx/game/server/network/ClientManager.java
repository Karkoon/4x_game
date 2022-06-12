package com.mygdx.game.server.network;

import io.vertx.core.http.ServerWebSocket;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class ClientManager {
  private final Map<Integer, ServerWebSocket> clients = new HashMap<>();

  private int nextId = 0;

  @Inject
  public ClientManager() {

  }

  public Map<Integer, ServerWebSocket> getClients() {
    return clients;
  }

  public int addClient(ServerWebSocket websocket) {
    clients.put(nextId, websocket);
    return nextId++;
  }

  public void removeClient(int id) {
    clients.remove(id);
  }
}
