package com.mygdx.game.server.network;

import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.model.GameRoom;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MoveEntityService {

  private final GameRoomSyncer syncer;

  @Inject
  MoveEntityService(
      GameRoomSyncer syncer
  ) {
    this.syncer = syncer;
  }

  public void moveEntity(int entityId, int x, int y, GameRoom room) {
    var coordinatesMapper = room.getGameInstance().getWorld().getMapper(Coordinates.class);
    var destination = coordinatesMapper.create(entityId);
    destination.setCoordinates(x, y);
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "Send position component");
    syncer.sendComponent(destination, entityId, room);
  }
}
