package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.EntityConfig;
import lombok.NonNull;

public abstract class EntityFactory<T extends EntityConfig> {

  protected final World world;
  protected final GameConfigAssets assets;

  protected EntityFactory(World world, GameConfigAssets assets) {
    this.world = world;
    this.assets = assets;
  }

  @NonNull
  public abstract void createEntity(int entityId, T entityConfig);

}
