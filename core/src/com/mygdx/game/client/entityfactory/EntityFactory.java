package com.mygdx.game.client.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.client.model.GameEntity;
import com.mygdx.game.config.EntityConfig;
import lombok.NonNull;

public abstract class EntityFactory<T extends EntityConfig> {

    protected final Engine engine;
    protected final Assets assets;

    public EntityFactory(Engine engine, Assets assets) {
        this.engine = engine;
        this.assets = assets;
    }

    @NonNull
    public abstract GameEntity<T> createEntity(T entityConfig, int x, int y);

}
