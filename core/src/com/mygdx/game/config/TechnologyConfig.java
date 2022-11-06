package com.mygdx.game.config;

import com.mygdx.game.core.model.TechnologyImpact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

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
  @NonNull
  private TechnologyImpact impact;
  @NonNull
  private List<Integer> dependencies;
}
