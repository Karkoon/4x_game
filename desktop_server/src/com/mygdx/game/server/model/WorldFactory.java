package com.mygdx.game.server.model;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.mygdx.game.server.di.GameInstanceScope;

import javax.inject.Inject;

@GameInstanceScope
public class WorldFactory {

  private final WorldConfiguration configuration;

  @Inject
  public WorldFactory(
      WorldConfiguration configuration
  ) {
    this.configuration = configuration;
  }

  public World get() {
    return new World(configuration);
  }

}
