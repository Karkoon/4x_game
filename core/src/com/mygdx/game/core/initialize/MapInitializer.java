package com.mygdx.game.core.initialize;

import com.mygdx.game.core.model.Coordinates;

import java.util.Map;

public interface MapInitializer {
  Map<Coordinates, Integer> initializeMap();
}
