package com.mygdx.game.client.model;

import com.badlogic.ashley.core.Entity;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;

@Singleton
@Setter
@Getter
public class GameState {

  private HashMap<Coordinates, Entity> fieldList;

  @Inject
  public GameState() {
  }
}
