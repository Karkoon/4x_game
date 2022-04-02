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
  }

  public @NonNull FieldConfig getAnyField() {
    return gameConfigs.getAny(FieldConfig.class);
  }

  private void putAllFields(@NonNull AssetManager manager) {
    Array<FieldConfig> fieldArray = new Array<>();
    manager.getAll(FieldConfig.class, fieldArray);
    gameConfigs.putAll(FieldConfig.class, fieldArray);
  }
}
