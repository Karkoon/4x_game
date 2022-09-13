package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.MoveRange;
import com.mygdx.game.core.util.DistanceUtil;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.GameRoom;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class MoveEntityService extends WorldService {

  @Inject
  MoveEntityService() {
    super();
  }

  public void moveEntity(int entityId, int x, int y, GameRoom room) {
    var world = room.getGameInstance().getWorld();
    var coordinatesMapper = world.getMapper(Coordinates.class);
    var destination = coordinatesMapper.get(entityId);

    var distance = DistanceUtil.distance(destination, new Coordinates(x, y));

    var moveRangeMapper = world.getMapper(MoveRange.class);
    var range = moveRangeMapper.get(entityId);
    var currentRange = range.getCurrentRange();
    log.info("Server - movement service");
    log.info("Current range: " + currentRange + "\nMovement distance: " + distance);

    if (currentRange < distance) {
      log.warning("Position out of move range!!!");
    }
    else{
      destination.setCoordinates(x, y);
      var updatedRange = currentRange - distance;
      range.setCurrentRange(updatedRange);

      log.info("Send position component");
      log.info("Unit has " + updatedRange + " move points left");
      setDirty(entityId, Coordinates.class, world);
//      TODO: Coś takiego powinienem zrobić? Czy o co chodzi z tym DirtyComponent???
      setDirty(entityId, MoveRange.class, world);
      world.process();
    }

  }
}
