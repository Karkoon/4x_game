package com.mygdx.game.server.model;

import lombok.NonNull;

import java.util.Collection;

public interface GameRoom {
  Collection<Client> getClients();

  void addClient(@NonNull Client client);

  int getNumberOfClients();

  String getRoomId();

  void startGame();

  void stopGame();

  GameInstance getGameInstance();

  void removeClient(Client client);
}
