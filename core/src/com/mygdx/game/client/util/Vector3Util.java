package com.mygdx.game.client.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public final class Vector3Util {

  public static Vector2 toVector2(Vector3 vec3) {
    return new Vector2(vec3.x, vec3.y);
  }

  private Vector3Util() {
  }
}
