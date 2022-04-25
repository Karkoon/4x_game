package com.mygdx.game.client.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.EntityConfig;
import lombok.NonNull;

public abstract class EntityFactory<T extends EntityConfig> {

  protected final Engine engine;
  protected final GameScreenAssets assets;

  protected EntityFactory(Engine engine, GameScreenAssets assets) {
    this.engine = engine;
    this.assets = assets;
  }

  @NonNull
  public abstract Entity createEntity(T entityConfig, int x, int y);

}
