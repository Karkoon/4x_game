package com.mygdx.game.client.model;

import com.mygdx.game.core.model.Coordinates;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Setter
@Getter
@Singleton
public class GameState {

  private Map<Coordinates, Integer> fields;

  @Inject
  public GameState() {

  }
}
