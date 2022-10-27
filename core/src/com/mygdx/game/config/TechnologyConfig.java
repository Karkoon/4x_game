package com.mygdx.game.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyConfig implements Config, TextureConfig {

  private long id;
  @NonNull
  private String name;
  @NonNull
  private String polishName;
  @NonNull
  private String textureName;
  private int x;
  private int y;
  private int requiredScience;
}
