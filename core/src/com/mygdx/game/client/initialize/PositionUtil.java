package com.mygdx.game.client.initialize;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.client.model.Coordinates;
import lombok.NonNull;

public final class PositionUtil {

  @NonNull
  public static Vector3 generateWorldPositionForCoords(Coordinates coord) {
    return new Vector3(coord.getX() * 190f + (coord.getY() % 2) * 95f, 0f, coord.getY() * 160f);
  }

  private PositionUtil() {
  }
}
