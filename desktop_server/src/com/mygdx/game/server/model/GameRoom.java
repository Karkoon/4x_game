package com.mygdx.game.server.model;

import com.mygdx.game.core.model.MapSize;
import lombok.NonNull;

import java.util.List;

public interface GameRoom {

  List<Client> getClients();

  void addClient(@NonNull Client client);

  void removeClient(Client client);

  int getOrderNumber(@NonNull Client client);

  Client getClientByToken(String token);

  int getNumberOfClients();

  String getRoomId();

  void setupGameInstance();

  void tearDownGameInstance();

  GameInstance getGameInstance();

  void setMapSize(MapSize mapSize);

  MapSize getMapSize();

  void setMapType(int mapType);

  int getMapType();
}
