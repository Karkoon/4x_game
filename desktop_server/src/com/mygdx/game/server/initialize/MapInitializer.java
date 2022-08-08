package com.mygdx.game.server.initialize;

import com.mygdx.game.server.initialize.generators.MapGeneratorsContainer;
import lombok.NonNull;

import javax.inject.Inject;

public class MapInitializer {

  private final MapGeneratorsContainer generators;
  private boolean initialized = false; // TODO: 16.06.2022 make it support multiple rooms

  @Inject
  public MapInitializer(
      @NonNull MapGeneratorsContainer generators
  ) {
    this.generators = generators;
  }

  public void initializeMap(int width, int height, long mapType) {
    if (initialized) {
      return;
    }
    initialized = true;
    generators.get(mapType).generateMap(width, height);
  }

}
