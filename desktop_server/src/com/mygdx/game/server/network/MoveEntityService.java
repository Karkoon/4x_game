package com.mygdx.game.server.network;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.model.GameRoom;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MoveEntityService {

  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final GameRoomSyncer syncer;
  private final GameRoom room;

  @Inject
  MoveEntityService(
      World world,
      GameRoomSyncer syncer,
      GameRoom room
  ) {
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.syncer = syncer;
    this.room = room;
  }

  public void moveEntity(int entityId, int x, int y) {
    coordinatesMapper.remove(entityId);
    var destination = coordinatesMapper.create(entityId); // todo: should it be written again as a System?
    destination.setCoordinates(x, y);
    log.info("Send position component");
    syncer.sendComponent(destination, entityId, room);
  }
}
