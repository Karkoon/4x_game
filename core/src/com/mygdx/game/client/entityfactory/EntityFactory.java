package com.mygdx.game.client.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.mygdx.assets.Assets;
import com.mygdx.config.EntityConfig;
import com.mygdx.game.client.model.GameEntity;
import lombok.NonNull;

public abstract class EntityFactory<T extends EntityConfig> {

    protected final Engine engine;
    protected final Assets assets;

    public EntityFactory(Engine engine, Assets assets) {
        this.engine = engine;
        this.assets = assets;
    }

    @NonNull
    public abstract GameEntity<T> createEntity(T entityConfig);

}
