package com.mygdx.game.server.initialize;

import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.initialize.generators.MapGeneratorsContainer;
import lombok.NonNull;

import javax.inject.Inject;

@GameInstanceScope
public class MapInitializer {

  private final MapGeneratorsContainer generators;

  @Inject
  public MapInitializer(
      @NonNull MapGeneratorsContainer generators
  ) {
    this.generators = generators;
  }

  public void initializeMap(int width, int height, long mapType) {
    generators.get(mapType).generateMap(width, height);
  }

}
