package com.mygdx.game.bot.util;

import com.mygdx.game.core.ecs.component.Coordinates;

import java.util.ArrayList;
import java.util.List;

public final class CoordinateUtil {

  public static final int INITIAL_WIDTH = 5;
  public static final int INITIAL_HEIGHT = 5;

  private static final int[][] coordinateMoves = {
          {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 0}
  };
  private static final int[][] coordinateMovesEven = {
          {-1, -1}, {0, -1}, {1, 0}, {0, 1}, {-1, 1}, {-1, 0}
  };

  private CoordinateUtil() {
  }

  public static List<Integer> getAvailableCoordinates(Coordinates coordinates) {

    List<Integer> availableCoordinates = new ArrayList<>();

    for (int i = 0; i < 6; i++) {
      int cX = 0;
      int cY = 0;
      if (coordinates.getY() % 2 == 1) {
        cX = coordinates.getX() + coordinateMoves[i][0];
        cY = coordinates.getY() + coordinateMoves[i][1];
      } else {
        cX = coordinates.getX() + coordinateMovesEven[i][0];
        cY = coordinates.getY() + coordinateMovesEven[i][1];
      }
      if (cX >= 0 && cX < INITIAL_WIDTH && cY >= 0 && cY < INITIAL_HEIGHT)
        availableCoordinates.add(i);
    }

    return availableCoordinates;
  }

  public static Coordinates mapMoveToCoordinate(Coordinates coordinates, Integer newMove) {
    int cX = 0;
    int cY = 0;
    if (coordinates.getY() % 2 == 1) {
      cX = coordinates.getX() + coordinateMoves[newMove][0];
      cY = coordinates.getY() + coordinateMoves[newMove][1];
    } else {
      cX = coordinates.getX() + coordinateMovesEven[newMove][0];
      cY = coordinates.getY() + coordinateMovesEven[newMove][1];
    }
    return new Coordinates(cX, cY);
  }
}
