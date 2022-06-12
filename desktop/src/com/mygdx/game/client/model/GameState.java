package com.mygdx.game.client.model;

import com.mygdx.game.core.model.Coordinates;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.Map;

@Setter
@Getter
public class GameState {

  private Map<Coordinates, Integer> fields;

  @Inject
  public GameState() {

  }
}
