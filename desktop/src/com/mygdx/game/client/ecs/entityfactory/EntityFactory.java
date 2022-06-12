package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.EntityConfig;
import com.mygdx.game.core.model.Coordinates;
import lombok.NonNull;

public abstract class EntityFactory<T extends EntityConfig> {

  protected final World world;
  protected final GameScreenAssets assets;

  protected EntityFactory(World world, GameScreenAssets assets) {
    this.world = world;
    this.assets = assets;
  }

  @NonNull
  public abstract int createEntity(T entityConfig, Coordinates coordinates);

}
