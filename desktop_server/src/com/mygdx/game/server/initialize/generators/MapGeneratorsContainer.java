package com.mygdx.game.server.initialize.generators;

import com.badlogic.gdx.utils.LongMap;
import com.mygdx.game.server.di.GameInstanceScope;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.Set;


@GameInstanceScope
public class MapGeneratorsContainer {
  private final LongMap<MapGenerator> mapTypes = new LongMap<>();

  @Inject
  public MapGeneratorsContainer(
      Set<MapGenerator> mapGeneratorSet
  ) {
    mapGeneratorSet.forEach(gen -> {
      if (!mapTypes.containsKey(gen.getId())) {
        mapTypes.put(gen.getId(), gen);
      } else {
        throw new IllegalArgumentException("map generators can't have the same id");
      }
    });
  }

  @NonNull
  public MapGenerator get(long type) {
    return mapTypes.get(type);
  }
}
