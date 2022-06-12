package com.mygdx.game.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {
  private int x;
  private int y;

  public static Coordinates of(int x, int y) {
    return new Coordinates(x, y);
  }
}
