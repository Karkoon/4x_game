package com.mygdx.game.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LongMap;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameConfigs {

  public static final int FIELD_MIN = 1;
  public static final int FIELD_MAX = 6;
  public static final int UNIT_MIN = 6;
  public static final int UNIT_MAX = 7;
  public static final int SUBFIELD_MIN = 7;
  public static final int SUBFIELD_MAX = 8;
  public static final int TECHNOLOGY_MIN = 8;
  public static final int TECHNOLOGY_MAX = 10;
  public static final int MAP_TYPE_MIN = 10;
  public static final int MAP_TYPE_MAX = 11;

  private final LongMap<Config> configMap = new LongMap<>();

  @Inject
  public GameConfigs() {
    super();
  }

  public <T extends Config> @NonNull T get(
      @NonNull final Class<T> entityClass,
      final long entityConfigId
  ) {
    return entityClass.cast(configMap.get(entityConfigId));
  }

  public @NonNull Config get(long entityConfigId) {
    return configMap.get(entityConfigId);
  }

  public <T extends Config> @NonNull T getAny(@NonNull final Class<T> entityClass) { // don't use later
    for (var next : configMap.values()) {
      if (entityClass.isInstance(next)) {
        return entityClass.cast(next);
      }
    }
    throw new IllegalArgumentException("No such Config saved");
  }

  public int size() {
    return configMap.size;
  }

  public void put(@NonNull final Config entityConfig) {
    configMap.put(entityConfig.getId(), entityConfig);
  }

  public <T extends Config> void putAll(@NonNull final Array<T> entityConfigArray) {
    for (int i = 0; i < entityConfigArray.size; i++) {
      put(entityConfigArray.get(i));
    }
  }

}
