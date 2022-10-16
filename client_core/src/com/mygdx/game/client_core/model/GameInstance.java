package com.mygdx.game.client_core.model;

import com.artemis.World;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.core.util.Updatable;
import lombok.Getter;

import javax.inject.Inject;

@GameInstanceScope
@Getter
public class GameInstance implements Updatable {

  private final World world;

  @Inject
  public GameInstance(
      World world
  ) {
    this.world = world;
  }

  @Override
  public void update(float delta) {
    world.setDelta(delta);
    world.process();
  }
}
