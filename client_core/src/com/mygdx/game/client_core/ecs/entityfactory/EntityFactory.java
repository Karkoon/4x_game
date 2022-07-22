package com.mygdx.game.client_core.ecs.entityfactory;

import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.EntityConfig;
import lombok.NonNull;

public abstract class EntityFactory<T extends EntityConfig> {

  protected final World world;
  protected final GameScreenAssets assets;

  protected EntityFactory(World world, GameScreenAssets assets) {
    this.world = world;
    this.assets = assets;
  }

  @NonNull
  public abstract void createEntity(T entityConfig, int entity);

}
