package com.mygdx.game.client.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.client.ecs.system.RenderSystem;
import com.mygdx.game.client.ecs.system.UnitMovementSystem;
import com.mygdx.game.client.util.Updatable;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameEngine implements Updatable {

  private final Engine engine;
  private final Array<EntitySystem> array = new Array<>();

  @Inject
  public GameEngine(@NonNull Engine engine,
                    @NonNull RenderSystem renderSystem,
                    @NonNull UnitMovementSystem unitMovementSystem) {
    this.engine = engine;
    array.add(renderSystem, unitMovementSystem);
    addEntitySystems();
  }

  private void addEntitySystems() {
    for (int i = 0; i < array.size; i++) {
      this.engine.addSystem(array.get(i));
    }
  }

  @Override
  public void update(float delta) {
    engine.update(delta);
  }
}
