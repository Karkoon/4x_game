package com.mygdx.game.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LongMap;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameConfigs {

  public final static int FIELD_MIN = 1;
  public final static int FIELD_MAX = 6;
  public final static int UNIT_MIN = 6;
  public final static int UNIT_MAX = 7;
  public final static int SUBFIELD_MIN = 7;
  public final static int SUBFIELD_MAX = 8;

  private final LongMap<EntityConfig> entityConfigMap = new LongMap<>();

  @Inject
  public GameConfigs() {
  }

  public <T extends EntityConfig> @NonNull T get(@NonNull final Class<T> entityClass,
                                        final long entityConfigId) {
    return entityClass.cast(entityConfigMap.get(entityConfigId));
  }

  public <T extends EntityConfig> @NonNull T getAny(@NonNull final Class<T> entityClass) { // don't use later
    for (EntityConfig next : entityConfigMap.values()) {
      if (entityClass.isInstance(next)) {
        return entityClass.cast(next);
      }
    }
    throw new IllegalArgumentException("No such EntityConfig saved");
  }

  public int size() {
    return entityConfigMap.size;
  }

  public void put(@NonNull final EntityConfig entityConfig) {
    entityConfigMap.put(entityConfig.getId(), entityConfig);
  }

  public <T extends EntityConfig> void putAll(@NonNull final Array<T> entityConfigArray) {
    for (int i = 0; i < entityConfigArray.size; i++) {
      put(entityConfigArray.get(i));
    }
  }

}
