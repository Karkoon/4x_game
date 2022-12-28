package com.mygdx.game.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LongMap;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameConfigs {

  public static final int FIELD_MIN = 1;
  public static final int FIELD_MAX = 12;
  public static final int UNIT_MIN = 101;
  public static final int UNIT_MAX = 118;
  public static final int SUBFIELD_MIN = 201;
  public static final int SUBFIELD_MAX = 212;
  public static final int TECHNOLOGY_MIN = 301;
  public static final int TECHNOLOGY_MAX = 317;
  public static final int MAP_TYPE_MIN = 401;
  public static final int MAP_TYPE_MAX = 404;
  public static final int BUILDING_MIN = 501;
  public static final int BUILDING_MAX = 502;
  public static final int CIV_MIN = 601;
  public static final int CIV_MAX = 603;

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

  public <T extends Config> @NonNull T getRandom(@NonNull final Class<T> entityClass) {
    Array<Config> configs = configMap.values().toArray();
    configs.shuffle();
    for (var next : configs) {
      if (entityClass.isInstance(next)) {
        return entityClass.cast(next);
      }
    }
    throw new IllegalArgumentException("No such Config saved");
  }

  public <T extends Config> Array<T> getAll(Class<T> configClass) {
    var array = new Array<T>(false, 16);
    configMap.values().forEach(config -> {
      if (configClass.isInstance(config)) {
        array.add(configClass.cast(config));
      }
    });
    return array;
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
