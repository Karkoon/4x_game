package com.mygdx.config;

import lombok.NonNull;

public interface EntityConfig {
    @NonNull
    int getId();
    @NonNull
    String getName();
    @NonNull
    String getModelPath();
}
