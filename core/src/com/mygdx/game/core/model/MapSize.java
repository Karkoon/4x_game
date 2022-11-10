package com.mygdx.game.core.model;

public enum MapSize {

  VERY_SMALL("Very small", 5, 5),
  SMALL("Small", 10, 10),
  NORMAL("Normal", 20, 20),
  BIG("Big", 30, 30);

  public final String name;
  public final int width;
  public final int height;

  MapSize(String name, int width, int height) {
    this.name = name;
    this.width = width;
    this.height = height;
  }

  @Override
  public String toString() {
    return name + " ( " + width + ":" + height + ")";
  }
}
