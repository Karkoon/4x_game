package com.mygdx.game.client.model;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.client.initialize.MapInitializer;
import com.mygdx.game.client.initialize.StartUnitInitializer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
@Setter
@Getter
public class GameState {

  private Map<Coordinates, Entity> fields;

  @Inject
  public GameState(@NonNull MapInitializer mapInitializer,
                   @NonNull StartUnitInitializer startUnitInitializer) {
    fields = mapInitializer.initializeMap();
    startUnitInitializer.initializeTestUnit(fields);
  }
}
