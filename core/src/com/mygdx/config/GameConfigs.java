package com.mygdx.config;

import com.badlogic.gdx.utils.Array;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class GameConfigs {

  @NonNull
  private final Map<Class<? extends EntityConfig>, Map<Integer, EntityConfig>> entityConfigMap;

  public GameConfigs() {
    entityConfigMap = new HashMap<>();
  }

  public <T extends EntityConfig> T get(@NonNull final Class<T> entityClass,
                                        @NonNull final Integer entityId) {
    return entityClass.cast(entityConfigMap.get(entityClass).get(entityId));
  }

  @NonNull
  public <T extends EntityConfig> T getAny(@NonNull final Class<T> entityClass) {
    return entityClass.cast(entityConfigMap.get(entityClass).values()
        .stream()
        .findAny().orElseThrow());
  }

  @NonNull
  public Array<EntityConfig> getAll() {
    var array = new Array<EntityConfig>();
    entityConfigMap.values()
        .stream()
        .flatMap(v -> v.values().stream())
        .forEach(array::add);
    return array;
  }

  public <T extends EntityConfig> void put(@NonNull final Class<T> entityClass,
                                           @NonNull final T entityConfig) {
    var singleItemArray = new Array<T>(1);
    singleItemArray.add(entityConfig);
    putAll(entityClass, singleItemArray);
  }

  public <T extends EntityConfig> void putAll(@NonNull final Class<T> entityClass,
                                              @NonNull final Array<T> entityArray) {
    if (entityConfigMap.containsKey(entityClass)) {
      var entityMap = entityConfigMap.get(entityClass);
      putEntityConfigsInMap(entityMap, entityArray);
    } else {
      var entityMap = new HashMap<Integer, EntityConfig>();
      putEntityConfigsInMap(entityMap, entityArray);
      entityConfigMap.put(entityClass, entityMap);
    }
  }

  private <T extends EntityConfig> void putEntityConfigsInMap(Map<Integer, EntityConfig> entityMap,
                                                              Array<T> entityArray) {
    for (var i = 0; i < entityArray.size; i++) {
      var entityConfig = entityArray.get(i);
      entityMap.put(entityConfig.getId(), entityConfig);
    }
  }
}
