package com.mygdx.game.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubFieldConfig implements EntityConfig, ModelConfig {
  private int id;
  @NonNull
  private String name;
  @NonNull
  private String polishName;
  @NonNull
  private String modelPath;
  @NonNull
  private String textureName;
}
