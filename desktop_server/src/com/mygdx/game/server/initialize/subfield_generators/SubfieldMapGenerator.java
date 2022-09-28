package com.mygdx.game.server.initialize.subfield_generators;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SubfieldMapGenerator {

  protected static final List<Coordinates> coordinatesList =
      List.of(
          new Coordinates(1, 0),
          new Coordinates(2, 0),
          new Coordinates(3, 0),
          new Coordinates(0, 1),
          new Coordinates(1, 1),
          new Coordinates(2, 1),
          new Coordinates(3, 1),
          new Coordinates(0, 2),
          new Coordinates(1, 2),
          new Coordinates(2, 2),
          new Coordinates(3, 2),
          new Coordinates(4, 2),
          new Coordinates(0, 3),
          new Coordinates(1, 3),
          new Coordinates(2, 3),
          new Coordinates(3, 3),
          new Coordinates(1, 4),
          new Coordinates(2, 4),
          new Coordinates(3, 4)
      );

  private final int id;

  public abstract IntArray generateSubfield(int parentId);

}
