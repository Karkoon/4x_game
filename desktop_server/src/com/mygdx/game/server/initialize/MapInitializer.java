package com.mygdx.game.server.initialize;

import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.initialize.field_generators.MapGeneratorsContainer;

import javax.inject.Inject;

@GameInstanceScope
public class MapInitializer {

  private final MapGeneratorsContainer generators;

  @Inject
  public MapInitializer(
      MapGeneratorsContainer generators
  ) {
    this.generators = generators;
  }

  public void initializeMap(int width, int height, long mapType) {
    generators.get(mapType).generateMap(width, height);
  }

}
