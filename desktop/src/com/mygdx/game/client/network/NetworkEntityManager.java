package com.mygdx.game.client.network;

import com.badlogic.gdx.utils.IntIntMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NetworkEntityManager {
  private final IntIntMap networkToWorldEntity = new IntIntMap();
  private final IntIntMap worldToNetworkEntity = new IntIntMap();

  @Inject
  public NetworkEntityManager() {

  }

  public void putEntity(int networkEntity, int worldEntity) {
    networkToWorldEntity.put(networkEntity, worldEntity);
    worldToNetworkEntity.put(worldEntity, networkEntity);
  }

  public void removeEntity(int entity, boolean isNetworkEntity) {
    if (isNetworkEntity) {
      var worldEntity = networkToWorldEntity.remove(entity, -1);
      worldToNetworkEntity.remove(worldEntity, -1);
    } else {
      var networkEntity = worldToNetworkEntity.remove(entity, -1);
      networkToWorldEntity.remove(networkEntity, -1);
    }
  }

  public int getWorldEntity(int networkEntity) {
    return networkToWorldEntity.get(networkEntity, -1);
  }

  public int getNetworkEntity(int worldEntity) {
    return worldToNetworkEntity.get(worldEntity, -1);
  }
}
