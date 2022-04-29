package com.mygdx.game.client.initialize;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.client.model.Coordinates;
import lombok.NonNull;

public final class PositionUtil {

  @NonNull
  public static Vector3 generateWorldPositionForCoords(Coordinates coordinates) {
    return new Vector3(coordinates.getX() * 190f + (coordinates.getY() % 2) * 95f, 0f, coordinates.getY() * 160f);
  }

  private PositionUtil() {
  }
}
