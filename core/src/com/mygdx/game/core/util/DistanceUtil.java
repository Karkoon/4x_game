package com.mygdx.game.core.util;

import com.mygdx.game.core.ecs.component.Coordinates;

public class DistanceUtil {

  public static int distance(Coordinates point1, Coordinates point2) {
    var ax = point1.getX() - (point1.getY() - (point1.getY() % 2)) / 2;
    var ay = point1.getY();
    var az = -ax-ay;

    var bx = point2.getX() - (point2.getY() - (point2.getY() % 2)) / 2;
    var by = point2.getY();
    var bz = -bx-by;

    return Math.max(Math.max(Math.abs(ax - bx), Math.abs(ay - by)), Math.abs(az - bz));
  }
}
