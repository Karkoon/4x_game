package com.mygdx.game.client.initialize;

import com.badlogic.gdx.math.Vector3;

public class DrawUtil {

  public static Vector3 generatePositionForField(int x, int y) {
    return new Vector3(x * 190 + (y % 2) * 95, 0, y * 160);
  }
}
