package com.mygdx.config;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import lombok.NonNull;

public class GameConfigsService {
    @NonNull
    private final GameConfigs gameConfigs;

    public GameConfigsService(@NonNull final AssetManager manager) {
        this.gameConfigs = new GameConfigs();
        putAllFields(manager);
        putAllUnits(manager);
    }

    public @NonNull FieldConfig getAnyField() {
        return gameConfigs.getAny(FieldConfig.class);
    }

    public @NonNull UnitConfig getAnyUnit() {
        return gameConfigs.getAny(UnitConfig.class);
    }

    private void putAllFields(@NonNull AssetManager manager) {
        var fieldConfigArray = new Array<FieldConfig>();
        manager.getAll(FieldConfig.class, fieldConfigArray);
        gameConfigs.putAll(FieldConfig.class, fieldConfigArray);
    }

    private void putAllUnits(@NonNull AssetManager manager) {
        var unitConfigArray = new Array<UnitConfig>();
        manager.getAll(UnitConfig.class, unitConfigArray);
        gameConfigs.putAll(UnitConfig.class, unitConfigArray);
    }
}
