package com.mygdx.game.server.initialize.field_generators;


import com.badlogic.gdx.utils.IntArray;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class MapGenerator {

  private final int id;

  protected MapGenerator(int id) {
    this.id = id;
  }

  public abstract IntArray generateMap(int width, int height);

}
