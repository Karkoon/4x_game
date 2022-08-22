package com.mygdx.game.server.initialize.subfield_generators;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.core.ecs.component.Coordinates;

import java.util.List;
import java.util.Objects;

public abstract class SubfieldMapGenerator {

  protected static final List<Coordinates> coordinatesList =
      List.of(
          new Coordinates(2, 0),
          new Coordinates(1, 1),
          new Coordinates(3, 1),
          new Coordinates(0, 2),
          new Coordinates(2, 2),
          new Coordinates(4, 2),
          new Coordinates(1, 3),
          new Coordinates(3, 3),
          new Coordinates(0, 4),
          new Coordinates(2, 4),
          new Coordinates(4, 4),
          new Coordinates(1, 5),
          new Coordinates(3, 5),
          new Coordinates(0, 6),
          new Coordinates(2, 6),
          new Coordinates(4, 6),
          new Coordinates(1, 7),
          new Coordinates(3, 7),
          new Coordinates(2, 8)
      );

  private final int id;

  protected SubfieldMapGenerator(int id) {
    this.id = id;
  }

  public abstract IntArray generateSubfield(int parentId);

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
    SubfieldMapGenerator that = (SubfieldMapGenerator) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
