package com.mygdx.game.server.initialize.generators;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class MapGenerator {

  private final int id;

  protected MapGenerator(int id) {
    this.id = id;
  }

  public abstract void generateMap(int width, int height);

}
