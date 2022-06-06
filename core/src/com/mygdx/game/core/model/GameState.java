package com.mygdx.game.core.model;

import com.mygdx.game.core.initialize.MapInitializer;
import com.mygdx.game.core.initialize.StartUnitInitializer;
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

  private Map<Coordinates, Integer> fields;

  @Inject
  public GameState(@NonNull MapInitializer mapInitializer,
                   @NonNull StartUnitInitializer startUnitInitializer) {
    fields = mapInitializer.initializeMap();
    startUnitInitializer.initializeTestUnit(fields.get(Coordinates.of(0, 0)));
  }
}
