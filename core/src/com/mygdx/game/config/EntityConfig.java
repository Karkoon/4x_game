package com.mygdx.game.config;

import lombok.NonNull;

public interface EntityConfig {
  @NonNull
  int getId();

  @NonNull
  String getName();

  @NonNull
  String getModelPath();
}
