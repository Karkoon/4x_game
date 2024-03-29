package com.mygdx.game.server.initialize.subfield_generators;

import com.badlogic.gdx.utils.LongMap;
import com.mygdx.game.server.di.GameInstanceScope;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.Set;


@GameInstanceScope
public class SubfieldMapGeneratorsContainer {

  private final LongMap<SubfieldMapGenerator> mapTypes = new LongMap<>();

  @Inject
  public SubfieldMapGeneratorsContainer(
      Set<SubfieldMapGenerator> subfieldMapGeneratorSet
  ) {
    subfieldMapGeneratorSet.forEach(gen -> {
      if (!mapTypes.containsKey(gen.getId())) {
        mapTypes.put(gen.getId(), gen);
      } else {
        throw new IllegalArgumentException("subfield map generators can't have the same id");
      }
    });
  }

  @NonNull
  public SubfieldMapGenerator get(long type) {
    if (mapTypes.containsKey(type)) {
      return mapTypes.get(type);
    } else {
      return mapTypes.get(0);
    }
  }
}
