package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.core.util.DistanceUtil;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.GameRoom;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.artemis.Aspect.all;

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
    var statsMapper = world.getMapper(Stats.class);

    var currentCoords = coordinatesMapper.get(entityId);
    var destinationCoords = new Coordinates(x, y);
    var distance = DistanceUtil.distance(currentCoords, destinationCoords);

    boolean canMove = true;
    var ownerComponentMapper = world.getMapper(Owner.class);
    var units = world.getAspectSubscriptionManager().get(all(Stats.class, Coordinates.class, Owner.class));
    for (int i = 0; i < units.getEntities().size(); i++) {
      var entity = units.getEntities().get(i);
      if (!coordinatesMapper.get(entity).equals(destinationCoords)){
        continue;
      }
      if (!ownerComponentMapper.get(entity).getToken()
              .equals(ownerComponentMapper.get(entityId).getToken())) {
        canMove = false;
        log.info("Cannot move on enemy's field!");
        break;
      }
    }

    if (canMove) {
      var stats = statsMapper.get(entityId);
      var currentRange = stats.getMoveRange();
      log.info("Server - movement service");
      log.info("Current range: " + currentRange + "\nMovement distance: " + distance);

      if (currentRange < distance) {
        log.warning("Position out of move range!!!");
      } else {
        currentCoords.setCoordinates(destinationCoords.getX(), destinationCoords.getY());
        var updatedRange = currentRange - distance;
        stats.setMoveRange(updatedRange);

        log.info("Send position component");
        log.info("Unit has " + updatedRange + " move points left");
        setDirty(entityId, Coordinates.class, world);
        setDirty(entityId, Stats.class, world);
      }
    }
    else {
      log.info("Cannot move! Field occupied by enemy!");
    }

  }
}
