package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.World;
import com.mygdx.game.server.ecs.component.DirtyComponents;

public abstract class WorldService {
  protected void setDirty(int entityId, Class component, World world) {
    var componentIndex = world.getComponentManager().getTypeFactory().getIndexFor(component);
    var dirtyComponentMapper = world.getMapper(DirtyComponents.class);
    dirtyComponentMapper.get(entityId).getDirtyComponents().set(componentIndex);
  }
}
