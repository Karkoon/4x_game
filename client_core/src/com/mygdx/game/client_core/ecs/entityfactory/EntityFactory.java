package com.mygdx.game.client_core.ecs.entityfactory;

import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.Config;
import lombok.NonNull;

public abstract class EntityFactory<T extends Config> {

  protected final World world;
  protected final GameScreenAssets assets;

  protected EntityFactory(World world, GameScreenAssets assets) {
    this.world = world;
    this.assets = assets;
  }

  @NonNull
  public abstract void createEntity(T entityConfig, int entity);

}
