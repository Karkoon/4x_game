package com.mygdx.game.server.model;

import com.badlogic.gdx.utils.ObjectMap;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameRoomManager {

  private final ObjectMap<String, GameRoom> rooms = new ObjectMap<>();
  private final GameRoomImpl.Factory gameRoomFactory;

  @Inject
  public GameRoomManager(
      GameRoomImpl.Factory gameRoomFactory
  ) {
    this.gameRoomFactory = gameRoomFactory;
  }


  public GameRoom getRoom(String roomId) {
    var room = rooms.get(roomId, null);
    if (room == null) {
      room = obtainRoom(roomId);
      rooms.put(roomId, room);
    }
    return room;
  }

  @NonNull
  private GameRoom obtainRoom(String roomId) {
    return gameRoomFactory.get(roomId);
  }
}
