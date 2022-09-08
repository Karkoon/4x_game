package com.mygdx.game.server.network;

import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.server.model.GameRoom;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MoveEntityService extends WorldService {

  @Inject
  MoveEntityService() {
    super();
  }

  public void moveEntity(int entityId, int x, int y, GameRoom room) {
    var world = room.getGameInstance().getWorld();
    var coordinatesMapper = world.getMapper(Coordinates.class);
    var destination = coordinatesMapper.get(entityId);
    destination.setCoordinates(x, y);
    log.info("Send position component");
    setDirty(entityId, Coordinates.class, world);
    world.process();
  }
}
