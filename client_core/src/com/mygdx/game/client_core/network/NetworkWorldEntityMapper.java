package com.mygdx.game.client_core.network;

import com.artemis.World;
import com.badlogic.gdx.utils.IntIntMap;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@GameInstanceScope
@Log
public class NetworkWorldEntityMapper {
  private final IntIntMap networkToWorldEntity = new IntIntMap();
  private final IntIntMap worldToNetworkEntity = new IntIntMap();
  private final Lazy<World> world;

  @Inject
  public NetworkWorldEntityMapper(
      Lazy<World> world
  ) {
    this.world = world;
  }

  public void putEntity(int networkEntity, int worldEntity) {
    networkToWorldEntity.put(networkEntity, worldEntity);
    worldToNetworkEntity.put(worldEntity, networkEntity);
  }

  public void removeEntity(int entity, boolean isNetworkEntity) {
    var world = this.world.get();
    if (isNetworkEntity) {
      var worldEntity = networkToWorldEntity.remove(entity, -1);
      worldToNetworkEntity.remove(worldEntity, -1);
      world.delete(worldEntity);
    } else {
      var networkEntity = worldToNetworkEntity.remove(entity, -1);
      var worldEntity = networkToWorldEntity.remove(networkEntity, -1);
      world.delete(worldEntity);
    }
  }

  public int getWorldEntity(int networkEntity) {
    var worldEntity = networkToWorldEntity.get(networkEntity, -1);
    if (worldEntity == -1) {
      worldEntity = world.get().create();
      log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "Creating now entity: network=" + networkEntity + " world=" + worldEntity);
      putEntity(networkEntity, worldEntity);
    }
    return worldEntity;
  }

  public int getNetworkEntity(int worldEntity) {
    return worldToNetworkEntity.get(worldEntity, -1);
  }
}
