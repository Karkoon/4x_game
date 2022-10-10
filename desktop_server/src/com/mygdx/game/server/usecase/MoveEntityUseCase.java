package com.mygdx.game.server.usecase;

import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.GameRoomSyncer;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MoveEntityUseCase {

  private final GameRoomSyncer syncer;

  @Inject
  MoveEntityUseCase(GameRoomSyncer syncer) {
    this.syncer = syncer;
  }

  public void moveEntity(int entityId, int x, int y, GameRoom room) {
    var coordinatesMapper = room.getGameInstance().getWorld().getMapper(Coordinates.class);
    var destination = coordinatesMapper.create(entityId);
    destination.setCoordinates(x, y);
    log.info("Send position component");
    syncer.sendComponent(destination, entityId, room);
  }
}
