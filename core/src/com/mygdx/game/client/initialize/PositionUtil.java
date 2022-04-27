package com.mygdx.game.client.initialize;

import com.badlogic.gdx.math.Vector3;
import lombok.NonNull;

public final class PositionUtil {

  @NonNull
  public static Vector3 generatePositionForField(int x, int y) {
    return new Vector3(x * 190f + (y % 2) * 95f, 0f, y * 160f);
  }

  private PositionUtil() {
  }
}
