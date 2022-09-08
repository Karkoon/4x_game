package com.mygdx.game.server.model;

import lombok.NonNull;

import java.util.List;

public interface GameRoom {
  List<Client> getClients();

  void addClient(@NonNull Client client);

  int getOrderNumber(@NonNull Client client);

  int getNumberOfClients();

  String getRoomId();

  void removeClient(Client client);

  void setupGameInstance();

  void tearDownGameInstance();

  GameInstance getGameInstance();

  Client getClientByToken(String token);
}
