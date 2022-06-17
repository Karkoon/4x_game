package com.mygdx.game.core.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lombok.NonNull;

public final class Vector3Util {

  @NonNull
  public static Vector2 toVector2(@NonNull Vector3 vec3) {
    return new Vector2(vec3.x, vec3.y);
  }

  private Vector3Util() {
  }
}
