package com.mygdx.game.server.initialize.field_generators;

import com.badlogic.gdx.utils.LongMap;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;


@Singleton
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
