package com.mygdx.config;

import lombok.NonNull;

public interface EntityConfig {
    @NonNull
    String getName();
    @NonNull
    String getModelPath();
}
