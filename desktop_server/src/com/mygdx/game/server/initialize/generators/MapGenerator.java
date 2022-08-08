package com.mygdx.game.server.initialize.generators;


import java.util.Objects;

public abstract class MapGenerator {

  private final int id;

  protected MapGenerator(int id) {
    this.id = id;
  }

  public abstract void generateMap(int width, int height);

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var that = (MapGenerator) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
