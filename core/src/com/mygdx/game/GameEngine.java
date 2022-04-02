package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.mygdx.game.client.Updatable;
import com.mygdx.game.client.entitysystem.RenderSystem;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameEngine implements Updatable {

    private final Engine engine;
    private final RenderSystem renderSystem;

    @Inject
    public GameEngine(@NonNull Engine engine, @NonNull RenderSystem renderSystem) {
        this.engine = engine;
        this.renderSystem = renderSystem;
        addEntitySystems();
    }

    private void addEntitySystems() {
        this.engine.addSystem(this.renderSystem);
    }

    @Override
    public void update(float delta) {
        engine.update(delta);
    }
}
