package com.mygdx.game.core.util;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.core.model.Coordinates;
import lombok.NonNull;

public final class PositionUtil {

  @NonNull
  public static Vector3 generateWorldPositionForCoords(Coordinates coordinates) {
    return new Vector3(coordinates.getX() * 190f + (coordinates.getY() % 2) * 95f, 0f, coordinates.getY() * 160f);
  }

  private PositionUtil() {
  }
}