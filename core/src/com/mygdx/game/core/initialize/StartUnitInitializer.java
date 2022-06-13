package com.mygdx.game.core.initialize;

import com.mygdx.game.core.model.Coordinates;

import java.util.Map;

public interface StartUnitInitializer {
  Map<Coordinates, Integer> initializeTestUnit(int field);
}
