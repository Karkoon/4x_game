package com.mygdx.game.config;

import lombok.NonNull;

public interface ModelConfig {
  @NonNull
  String getModelPath();

  @NonNull
  String getTextureName();
}
