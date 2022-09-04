package com.mygdx.game.server.network;

import com.artemis.World;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.component.DirtyComponents;
import com.mygdx.game.server.model.GameRoom;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MoveEntityService {

  @Inject
  MoveEntityService() {
    super();
  }

  public void moveEntity(int entityId, int x, int y, GameRoom room) {
    var coordinatesMapper = room.getGameInstance().getWorld().getMapper(Coordinates.class);
    var destination = coordinatesMapper.create(entityId);
    destination.setCoordinates(x, y);
    log.info("Send position component");
    setDirty(entityId, Coordinates.class, room.getGameInstance().getWorld());
  }

  private void setDirty(int entityId, Class component, World world) {
    var componentIndex = world.getComponentManager().getTypeFactory().getIndexFor(component);
    var dirtyComponentMapper = world.getMapper(DirtyComponents.class);
    dirtyComponentMapper.get(entityId).getDirtyComponents().set(componentIndex);
  }
}
