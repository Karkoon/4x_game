package com.mygdx.config;

import com.badlogic.gdx.utils.Array;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class GameConfigs {

    @NonNull
    private final Map<Class<? extends EntityConfig>, Map<String, EntityConfig>> entityConfigListMap;

    public GameConfigs() {
        entityConfigListMap = new HashMap<>();
    }

    public <T extends EntityConfig> T get(@NonNull final Class<T> fieldClass, @NonNull final String entityName) {
        return fieldClass.cast(entityConfigListMap.get(fieldClass).get(entityName));
    }

    @NonNull
    public <T extends EntityConfig> T getAny(@NonNull final Class<T> fieldClass) {
        return fieldClass.cast(entityConfigListMap.get(fieldClass).values()
                .stream()
                .findAny().orElseThrow());
    }

    public <T extends EntityConfig> void put(@NonNull final Class<T> clazz, @NonNull final T entityConfig) {
        var singleItemArray = new Array<T>(1);
        singleItemArray.add(entityConfig);
        putAll(clazz, singleItemArray);
    }

    public <T extends EntityConfig> void putAll(Class<T> clazz, Array<T> array) {
        if (entityConfigListMap.containsKey(clazz)) {
            var entityMap = entityConfigListMap.get(clazz);
            for (var i = 0; i < array.size; i++) {
                var entityConfig = array.get(i);
                entityMap.put(entityConfig.getName(), entityConfig);
            }
        } else {
            var entityMap = new HashMap<String, EntityConfig>();
            for (var i = 0; i < array.size; i++) {
                var entityConfig = array.get(i);
                entityMap.put(entityConfig.getName(), entityConfig);
            }
            entityConfigListMap.put(clazz, entityMap);
        }
    }
}
