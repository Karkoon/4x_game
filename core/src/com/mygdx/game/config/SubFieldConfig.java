package com.mygdx.game.config;

import com.mygdx.game.core.model.MaterialUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SubFieldConfig implements Config, ModelConfig {

  private long id;
  @NonNull
  private String name;
  @NonNull
  private String polishName;
  @NonNull
  private String modelPath;
  @NonNull
  private String textureName;
  @NonNull
  private List<List<MaterialUnit>> materialProductions;
}
