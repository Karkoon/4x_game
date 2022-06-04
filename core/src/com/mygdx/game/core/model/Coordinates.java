package com.mygdx.game.core.model;

import lombok.Data;

@Data
public class Coordinates {
  private final int x;
  private final int y;

  public static Coordinates of(int x, int y) {
    return new Coordinates(x, y);
  }
}
