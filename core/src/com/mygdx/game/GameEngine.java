package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.mygdx.game.client.Updatable;
import com.mygdx.game.client.component.UnitMovementComp;
import com.mygdx.game.client.entitysystem.RenderSystem;
import com.mygdx.game.client.entitysystem.UnitMovementSystem;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameEngine implements Updatable {

  private final Engine engine;
  private final RenderSystem renderSystem;
  private final UnitMovementSystem unitMovementSystem;

  @Inject
  public GameEngine(@NonNull Engine engine, @NonNull RenderSystem renderSystem, @NonNull UnitMovementSystem unitMovementSystem) {
    this.engine = engine;
    this.renderSystem = renderSystem;
    this.unitMovementSystem = unitMovementSystem;
    addEntitySystems();
  }

  private void addEntitySystems() {
    this.engine.addSystem(this.renderSystem);
    this.engine.addSystem(this.unitMovementSystem);
  }

  @Override
  public void update(float delta) {
    engine.update(delta);
  }
}
